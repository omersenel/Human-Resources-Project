package com.metasis.ikamet.repository;

import com.metasis.ikamet.domain.File;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the File entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileRepository extends R2dbcRepository<File, Long>, FileRepositoryInternal {
    @Query("SELECT * FROM file entity WHERE entity.candidate_id = :id")
    Flux<File> findByCandidate(Long id);

    @Query("SELECT * FROM file entity WHERE entity.candidate_id IS NULL")
    Flux<File> findAllWhereCandidateIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<File> findAll();

    @Override
    Mono<File> findById(Long id);


    @Override
    <S extends File> Mono<S> save(S entity);
}

interface FileRepositoryInternal {
    <S extends File> Mono<S> insert(S entity);
    <S extends File> Mono<S> save(S entity);
    Mono<Integer> update(File entity);

    Flux<File> findAll();
    Mono<File> findById(Long id);
    Flux<File> findAllBy(Pageable pageable);
    Flux<File> findAllBy(Pageable pageable, Criteria criteria);
}
