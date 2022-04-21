package com.metasis.ikamet.web.rest;

import com.metasis.ikamet.domain.HrInterview;
import com.metasis.ikamet.repository.HrInterviewRepository;
import com.metasis.ikamet.service.HrInterviewService;
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
 * REST controller for managing {@link com.metasis.ikamet.domain.HrInterview}.
 */
@RestController
@RequestMapping("/api")
public class HrInterviewResource {

    private final Logger log = LoggerFactory.getLogger(HrInterviewResource.class);

    private static final String ENTITY_NAME = "hrInterview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HrInterviewService hrInterviewService;

    private final HrInterviewRepository hrInterviewRepository;

    public HrInterviewResource(HrInterviewService hrInterviewService, HrInterviewRepository hrInterviewRepository) {
        this.hrInterviewService = hrInterviewService;
        this.hrInterviewRepository = hrInterviewRepository;
    }

    /**
     * {@code POST  /hr-interviews} : Create a new hrInterview.
     *
     * @param hrInterview the hrInterview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hrInterview, or with status {@code 400 (Bad Request)} if the hrInterview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hr-interviews")
    public Mono<ResponseEntity<HrInterview>> createHrInterview(@RequestBody HrInterview hrInterview) throws URISyntaxException {
        log.debug("REST request to save HrInterview : {}", hrInterview);
        if (hrInterview.getId() != null) {
            throw new BadRequestAlertException("A new hrInterview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return hrInterviewService
            .save(hrInterview)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/hr-interviews/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /hr-interviews/:id} : Updates an existing hrInterview.
     *
     * @param id the id of the hrInterview to save.
     * @param hrInterview the hrInterview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hrInterview,
     * or with status {@code 400 (Bad Request)} if the hrInterview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hrInterview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hr-interviews/{id}")
    public Mono<ResponseEntity<HrInterview>> updateHrInterview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HrInterview hrInterview
    ) throws URISyntaxException {
        log.debug("REST request to update HrInterview : {}, {}", id, hrInterview);
        if (hrInterview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hrInterview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return hrInterviewRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return hrInterviewService
                    .save(hrInterview)
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
     * {@code PATCH  /hr-interviews/:id} : Partial updates given fields of an existing hrInterview, field will ignore if it is null
     *
     * @param id the id of the hrInterview to save.
     * @param hrInterview the hrInterview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hrInterview,
     * or with status {@code 400 (Bad Request)} if the hrInterview is not valid,
     * or with status {@code 404 (Not Found)} if the hrInterview is not found,
     * or with status {@code 500 (Internal Server Error)} if the hrInterview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hr-interviews/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HrInterview>> partialUpdateHrInterview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HrInterview hrInterview
    ) throws URISyntaxException {
        log.debug("REST request to partial update HrInterview partially : {}, {}", id, hrInterview);
        if (hrInterview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hrInterview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return hrInterviewRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HrInterview> result = hrInterviewService.partialUpdate(hrInterview);

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
     * {@code GET  /hr-interviews} : get all the hrInterviews.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hrInterviews in body.
     */
    @GetMapping("/hr-interviews")
    public Mono<List<HrInterview>> getAllHrInterviews() {
        log.debug("REST request to get all HrInterviews");
        return hrInterviewService.findAll().collectList();
    }

    /**
     * {@code GET  /hr-interviews} : get all the hrInterviews as a stream.
     * @return the {@link Flux} of hrInterviews.
     */
    @GetMapping(value = "/hr-interviews", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<HrInterview> getAllHrInterviewsAsStream() {
        log.debug("REST request to get all HrInterviews as a stream");
        return hrInterviewService.findAll();
    }

    /**
     * {@code GET  /hr-interviews/:id} : get the "id" hrInterview.
     *
     * @param id the id of the hrInterview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hrInterview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hr-interviews/{id}")
    public Mono<ResponseEntity<HrInterview>> getHrInterview(@PathVariable Long id) {
        log.debug("REST request to get HrInterview : {}", id);
        Mono<HrInterview> hrInterview = hrInterviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hrInterview);
    }

    /**
     * {@code DELETE  /hr-interviews/:id} : delete the "id" hrInterview.
     *
     * @param id the id of the hrInterview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hr-interviews/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteHrInterview(@PathVariable Long id) {
        log.debug("REST request to delete HrInterview : {}", id);
        return hrInterviewService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
