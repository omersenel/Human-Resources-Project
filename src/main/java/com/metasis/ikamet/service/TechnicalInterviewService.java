package com.metasis.ikamet.service;

import com.metasis.ikamet.domain.TechnicalInterview;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link TechnicalInterview}.
 */
public interface TechnicalInterviewService {
    /**
     * Save a technicalInterview.
     *
     * @param technicalInterview the entity to save.
     * @return the persisted entity.
     */
    Mono<TechnicalInterview> save(TechnicalInterview technicalInterview);

    /**
     * Partially updates a technicalInterview.
     *
     * @param technicalInterview the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TechnicalInterview> partialUpdate(TechnicalInterview technicalInterview);

    /**
     * Get all the technicalInterviews.
     *
     * @return the list of entities.
     */
    Flux<TechnicalInterview> findAll();

    /**
     * Returns the number of technicalInterviews available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" technicalInterview.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TechnicalInterview> findOne(Long id);

    /**
     * Delete the "id" technicalInterview.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
