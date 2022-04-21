package com.metasis.ikamet.repository;

import com.metasis.ikamet.domain.Candidate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Candidate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateRepository extends R2dbcRepository<Candidate, Long>, CandidateRepositoryInternal {
    // just to avoid having unambigous methods
    @Override
    Flux<Candidate> findAll();

    @Override
    Mono<Candidate> findById(Long id);

    @Override
    <S extends Candidate> Mono<S> save(S entity);
}

interface CandidateRepositoryInternal {
    <S extends Candidate> Mono<S> insert(S entity);
    <S extends Candidate> Mono<S> save(S entity);
    Mono<Integer> update(Candidate entity);

    Flux<Candidate> findAll();
    Mono<Candidate> findById(Long id);
    Flux<Candidate> findAllBy(Pageable pageable);
    Flux<Candidate> findAllBy(Pageable pageable, Criteria criteria);
}
