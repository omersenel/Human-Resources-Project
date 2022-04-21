package com.metasis.ikamet.repository;

import com.metasis.ikamet.domain.Process;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Process entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessRepository extends R2dbcRepository<Process, Long>, ProcessRepositoryInternal {
    @Query("SELECT * FROM process entity WHERE entity.candidate_id = :id")
    Flux<Process> findByCandidate(Long id);

    @Query("SELECT * FROM process entity WHERE entity.candidate_id IS NULL")
    Flux<Process> findAllWhereCandidateIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Process> findAll();

    @Override
    Mono<Process> findById(Long id);

    @Override
    <S extends Process> Mono<S> save(S entity);
}

interface ProcessRepositoryInternal {
    <S extends Process> Mono<S> insert(S entity);
    <S extends Process> Mono<S> save(S entity);
    Mono<Integer> update(Process entity);

    Flux<Process> findAll();
    Mono<Process> findById(Long id);
    Flux<Process> findAllBy(Pageable pageable);
    Flux<Process> findAllBy(Pageable pageable, Criteria criteria);
}
