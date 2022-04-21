package com.metasis.ikamet.service;

import com.metasis.ikamet.domain.File;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link File}.
 */
public interface FileService {
    /**
     * Save a file.
     *
     * @param file the entity to save.
     * @return the persisted entity.
     */
    Mono<File> save(File file);

    /**
     * Partially updates a file.
     *
     * @param file the entity to update partially.
     * @return the persisted entity.
     */
    Mono<File> partialUpdate(File file);

    /**
     * Get all the files.
     *
     * @return the list of entities.
     */
    Flux<File> findAll();

    /**
     * Returns the number of files available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" file.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<File> findOne(Long id);

    /**
     * Delete the "id" file.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
