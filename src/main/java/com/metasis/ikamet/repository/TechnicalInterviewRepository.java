package com.metasis.ikamet.repository;

import com.metasis.ikamet.domain.TechnicalInterview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the TechnicalInterview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TechnicalInterviewRepository extends R2dbcRepository<TechnicalInterview, Long>, TechnicalInterviewRepositoryInternal {
    @Query("SELECT * FROM technical_interview entity WHERE entity.process_id = :id")
    Flux<TechnicalInterview> findByProcess(Long id);

    @Query("SELECT * FROM technical_interview entity WHERE entity.process_id IS NULL")
    Flux<TechnicalInterview> findAllWhereProcessIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<TechnicalInterview> findAll();

    @Override
    Mono<TechnicalInterview> findById(Long id);

    @Override
    <S extends TechnicalInterview> Mono<S> save(S entity);
}

interface TechnicalInterviewRepositoryInternal {
    <S extends TechnicalInterview> Mono<S> insert(S entity);
    <S extends TechnicalInterview> Mono<S> save(S entity);
    Mono<Integer> update(TechnicalInterview entity);

    Flux<TechnicalInterview> findAll();
    Mono<TechnicalInterview> findById(Long id);
    Flux<TechnicalInterview> findAllBy(Pageable pageable);
    Flux<TechnicalInterview> findAllBy(Pageable pageable, Criteria criteria);
}
