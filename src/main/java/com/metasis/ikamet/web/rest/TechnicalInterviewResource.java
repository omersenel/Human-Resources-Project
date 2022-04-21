package com.metasis.ikamet.web.rest;

import com.metasis.ikamet.domain.TechnicalInterview;
import com.metasis.ikamet.repository.TechnicalInterviewRepository;
import com.metasis.ikamet.service.TechnicalInterviewService;
import com.metasis.ikamet.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.metasis.ikamet.domain.TechnicalInterview}.
 */
@RestController
@RequestMapping("/api")
public class TechnicalInterviewResource {

    private final Logger log = LoggerFactory.getLogger(TechnicalInterviewResource.class);

    private static final String ENTITY_NAME = "technicalInterview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechnicalInterviewService technicalInterviewService;

    private final TechnicalInterviewRepository technicalInterviewRepository;

    public TechnicalInterviewResource(
        TechnicalInterviewService technicalInterviewService,
        TechnicalInterviewRepository technicalInterviewRepository
    ) {
        this.technicalInterviewService = technicalInterviewService;
        this.technicalInterviewRepository = technicalInterviewRepository;
    }

    /**
     * {@code POST  /technical-interviews} : Create a new technicalInterview.
     *
     * @param technicalInterview the technicalInterview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new technicalInterview, or with status {@code 400 (Bad Request)} if the technicalInterview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/technical-interviews")
    public Mono<ResponseEntity<TechnicalInterview>> createTechnicalInterview(@RequestBody TechnicalInterview technicalInterview)
        throws URISyntaxException {
        log.debug("REST request to save TechnicalInterview : {}", technicalInterview);
        if (technicalInterview.getId() != null) {
            throw new BadRequestAlertException("A new technicalInterview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return technicalInterviewService
            .save(technicalInterview)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/technical-interviews/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /technical-interviews/:id} : Updates an existing technicalInterview.
     *
     * @param id the id of the technicalInterview to save.
     * @param technicalInterview the technicalInterview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technicalInterview,
     * or with status {@code 400 (Bad Request)} if the technicalInterview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the technicalInterview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/technical-interviews/{id}")
    public Mono<ResponseEntity<TechnicalInterview>> updateTechnicalInterview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TechnicalInterview technicalInterview
    ) throws URISyntaxException {
        log.debug("REST request to update TechnicalInterview : {}, {}", id, technicalInterview);
        if (technicalInterview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technicalInterview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return technicalInterviewRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return technicalInterviewService
                    .save(technicalInterview)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /technical-interviews/:id} : Partial updates given fields of an existing technicalInterview, field will ignore if it is null
     *
     * @param id the id of the technicalInterview to save.
     * @param technicalInterview the technicalInterview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technicalInterview,
     * or with status {@code 400 (Bad Request)} if the technicalInterview is not valid,
     * or with status {@code 404 (Not Found)} if the technicalInterview is not found,
     * or with status {@code 500 (Internal Server Error)} if the technicalInterview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/technical-interviews/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TechnicalInterview>> partialUpdateTechnicalInterview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TechnicalInterview technicalInterview
    ) throws URISyntaxException {
        log.debug("REST request to partial update TechnicalInterview partially : {}, {}", id, technicalInterview);
        if (technicalInterview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technicalInterview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return technicalInterviewRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TechnicalInterview> result = technicalInterviewService.partialUpdate(technicalInterview);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /technical-interviews} : get all the technicalInterviews.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of technicalInterviews in body.
     */
    @GetMapping("/technical-interviews")
    public Mono<List<TechnicalInterview>> getAllTechnicalInterviews() {
        log.debug("REST request to get all TechnicalInterviews");
        return technicalInterviewService.findAll().collectList();
    }

    /**
     * {@code GET  /technical-interviews} : get all the technicalInterviews as a stream.
     * @return the {@link Flux} of technicalInterviews.
     */
    @GetMapping(value = "/technical-interviews", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<TechnicalInterview> getAllTechnicalInterviewsAsStream() {
        log.debug("REST request to get all TechnicalInterviews as a stream");
        return technicalInterviewService.findAll();
    }

    /**
     * {@code GET  /technical-interviews/:id} : get the "id" technicalInterview.
     *
     * @param id the id of the technicalInterview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the technicalInterview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/technical-interviews/{id}")
    public Mono<ResponseEntity<TechnicalInterview>> getTechnicalInterview(@PathVariable Long id) {
        log.debug("REST request to get TechnicalInterview : {}", id);
        Mono<TechnicalInterview> technicalInterview = technicalInterviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(technicalInterview);
    }

    /**
     * {@code DELETE  /technical-interviews/:id} : delete the "id" technicalInterview.
     *
     * @param id the id of the technicalInterview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/technical-interviews/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTechnicalInterview(@PathVariable Long id) {
        log.debug("REST request to delete TechnicalInterview : {}", id);
        return technicalInterviewService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
