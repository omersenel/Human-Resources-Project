package com.metasis.ikamet.service;

import com.metasis.ikamet.domain.Process;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link Process}.
 */
public interface ProcessService {
    /**
     * Save a process.
     *
     * @param process the entity to save.
     * @return the persisted entity.
     */
    Mono<Process> save(Process process);

    /**
     * Partially updates a process.
     *
     * @param process the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Process> partialUpdate(Process process);

    /**
     * Get all the processes.
     *
     * @return the list of entities.
     */
    Flux<Process> findAll();

    /**
     * Returns the number of processes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" process.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Process> findOne(Long id);

    /**
     * Delete the "id" process.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
