package com.metasis.ikamet.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.metasis.ikamet.domain.TechnicalInterview;
import com.metasis.ikamet.repository.rowmapper.ProcessRowMapper;
import com.metasis.ikamet.repository.rowmapper.TechnicalInterviewRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the TechnicalInterview entity.
 */
@SuppressWarnings("unused")
class TechnicalInterviewRepositoryInternalImpl implements TechnicalInterviewRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProcessRowMapper processMapper;
    private final TechnicalInterviewRowMapper technicalinterviewMapper;

    private static final Table entityTable = Table.aliased("technical_interview", EntityManager.ENTITY_ALIAS);
    private static final Table processTable = Table.aliased("process", "process");

    public TechnicalInterviewRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProcessRowMapper processMapper,
        TechnicalInterviewRowMapper technicalinterviewMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.processMapper = processMapper;
        this.technicalinterviewMapper = technicalinterviewMapper;
    }

    @Override
    public Flux<TechnicalInterview> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<TechnicalInterview> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<TechnicalInterview> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = TechnicalInterviewSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProcessSqlHelper.getColumns(processTable, "process"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(processTable)
            .on(Column.create("process_id", entityTable))
            .equals(Column.create("id", processTable));

        String select = entityManager.createSelect(selectFrom, TechnicalInterview.class, pageable, criteria);
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
    public Flux<TechnicalInterview> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<TechnicalInterview> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private TechnicalInterview process(Row row, RowMetadata metadata) {
        TechnicalInterview entity = technicalinterviewMapper.apply(row, "e");
        entity.setProcess(processMapper.apply(row, "process"));
        return entity;
    }

    @Override
    public <S extends TechnicalInterview> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends TechnicalInterview> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update TechnicalInterview with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(TechnicalInterview entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
