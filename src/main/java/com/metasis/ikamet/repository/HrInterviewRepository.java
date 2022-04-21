package com.metasis.ikamet.repository;

import com.metasis.ikamet.domain.HrInterview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the HrInterview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HrInterviewRepository extends R2dbcRepository<HrInterview, Long>, HrInterviewRepositoryInternal {
    @Query("SELECT * FROM hr_interview entity WHERE entity.process_id = :id")
    Flux<HrInterview> findByProcess(Long id);

    @Query("SELECT * FROM hr_interview entity WHERE entity.process_id IS NULL")
    Flux<HrInterview> findAllWhereProcessIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<HrInterview> findAll();

    @Override
    Mono<HrInterview> findById(Long id);

    @Override
    <S extends HrInterview> Mono<S> save(S entity);
}

interface HrInterviewRepositoryInternal {
    <S extends HrInterview> Mono<S> insert(S entity);
    <S extends HrInterview> Mono<S> save(S entity);
    Mono<Integer> update(HrInterview entity);

    Flux<HrInterview> findAll();
    Mono<HrInterview> findById(Long id);
    Flux<HrInterview> findAllBy(Pageable pageable);
    Flux<HrInterview> findAllBy(Pageable pageable, Criteria criteria);
}
