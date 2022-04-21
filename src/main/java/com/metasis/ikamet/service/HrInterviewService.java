package com.metasis.ikamet.service;

import com.metasis.ikamet.domain.HrInterview;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link HrInterview}.
 */
public interface HrInterviewService {
    /**
     * Save a hrInterview.
     *
     * @param hrInterview the entity to save.
     * @return the persisted entity.
     */
    Mono<HrInterview> save(HrInterview hrInterview);

    /**
     * Partially updates a hrInterview.
     *
     * @param hrInterview the entity to update partially.
     * @return the persisted entity.
     */
    Mono<HrInterview> partialUpdate(HrInterview hrInterview);

    /**
     * Get all the hrInterviews.
     *
     * @return the list of entities.
     */
    Flux<HrInterview> findAll();

    /**
     * Returns the number of hrInterviews available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" hrInterview.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<HrInterview> findOne(Long id);

    /**
     * Delete the "id" hrInterview.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
