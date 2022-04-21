package com.metasis.ikamet.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.metasis.ikamet.domain.Process;
import com.metasis.ikamet.repository.rowmapper.CandidateRowMapper;
import com.metasis.ikamet.repository.rowmapper.ProcessRowMapper;
import com.metasis.ikamet.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Process entity.
 */
@SuppressWarnings("unused")
class ProcessRepositoryInternalImpl implements ProcessRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CandidateRowMapper candidateMapper;
    private final ProcessRowMapper processMapper;

    private static final Table entityTable = Table.aliased("process", EntityManager.ENTITY_ALIAS);
    private static final Table candidateTable = Table.aliased("candidate", "candidate");

    public ProcessRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CandidateRowMapper candidateMapper,
        ProcessRowMapper processMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.candidateMapper = candidateMapper;
        this.processMapper = processMapper;
    }

    @Override
    public Flux<Process> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Process> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Process> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = ProcessSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CandidateSqlHelper.getColumns(candidateTable, "candidate"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(candidateTable)
            .on(Column.create("candidate_id", entityTable))
            .equals(Column.create("id", candidateTable));

        String select = entityManager.createSelect(selectFrom, Process.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(crit ->
                new StringBuilder(select)
                    .append(" ")
                    .append("WHERE")
                    .append(" ")
                    .append(alias)
                    .append(".")
                    .append(crit.toString())
                    .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<Process> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Process> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Process process(Row row, RowMetadata metadata) {
        Process entity = processMapper.apply(row, "e");
        entity.setCandidate(candidateMapper.apply(row, "candidate"));
        return entity;
    }

    @Override
    public <S extends Process> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Process> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update Process with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(Process entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
