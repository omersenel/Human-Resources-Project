package com.metasis.ikamet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.metasis.ikamet.IntegrationTest;
import com.metasis.ikamet.domain.TechnicalInterview;
import com.metasis.ikamet.repository.TechnicalInterviewRepository;
import com.metasis.ikamet.service.EntityManager;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link TechnicalInterviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class TechnicalInterviewResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_SCORE = 1D;
    private static final Double UPDATED_SCORE = 2D;

    private static final String DEFAULT_TECHNICAL_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_TECHNICAL_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_EDIT_BY = "AAAAAAAAAA";
    private static final String UPDATED_EDIT_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/technical-interviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TechnicalInterviewRepository technicalInterviewRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TechnicalInterview technicalInterview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechnicalInterview createEntity(EntityManager em) {
        TechnicalInterview technicalInterview = new TechnicalInterview()
            .date(DEFAULT_DATE)
            .score(DEFAULT_SCORE)
            .technicalStatus(DEFAULT_TECHNICAL_STATUS)
            .notes(DEFAULT_NOTES)
            .editBy(DEFAULT_EDIT_BY);
        return technicalInterview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TechnicalInterview createUpdatedEntity(EntityManager em) {
        TechnicalInterview technicalInterview = new TechnicalInterview()
            .date(UPDATED_DATE)
            .score(UPDATED_SCORE)
            .technicalStatus(UPDATED_TECHNICAL_STATUS)
            .notes(UPDATED_NOTES)
            .editBy(UPDATED_EDIT_BY);
        return technicalInterview;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TechnicalInterview.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        technicalInterview = createEntity(em);
    }

    @Test
    void createTechnicalInterview() throws Exception {
        int databaseSizeBeforeCreate = technicalInterviewRepository.findAll().collectList().block().size();
        // Create the TechnicalInterview
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicalInterview))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TechnicalInterview in the database
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeCreate + 1);
        TechnicalInterview testTechnicalInterview = technicalInterviewList.get(technicalInterviewList.size() - 1);
        assertThat(testTechnicalInterview.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTechnicalInterview.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testTechnicalInterview.getTechnicalStatus()).isEqualTo(DEFAULT_TECHNICAL_STATUS);
        assertThat(testTechnicalInterview.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testTechnicalInterview.getEditBy()).isEqualTo(DEFAULT_EDIT_BY);
    }

    @Test
    void createTechnicalInterviewWithExistingId() throws Exception {
        // Create the TechnicalInterview with an existing ID
        technicalInterview.setId(1L);

        int databaseSizeBeforeCreate = technicalInterviewRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicalInterview))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TechnicalInterview in the database
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTechnicalInterviewsAsStream() {
        // Initialize the database
        technicalInterviewRepository.save(technicalInterview).block();

        List<TechnicalInterview> technicalInterviewList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(TechnicalInterview.class)
            .getResponseBody()
            .filter(technicalInterview::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(technicalInterviewList).isNotNull();
        assertThat(technicalInterviewList).hasSize(1);
        TechnicalInterview testTechnicalInterview = technicalInterviewList.get(0);
        assertThat(testTechnicalInterview.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTechnicalInterview.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testTechnicalInterview.getTechnicalStatus()).isEqualTo(DEFAULT_TECHNICAL_STATUS);
        assertThat(testTechnicalInterview.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testTechnicalInterview.getEditBy()).isEqualTo(DEFAULT_EDIT_BY);
    }

    @Test
    void getAllTechnicalInterviews() {
        // Initialize the database
        technicalInterviewRepository.save(technicalInterview).block();

        // Get all the technicalInterviewList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(technicalInterview.getId().intValue()))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE.toString()))
            .jsonPath("$.[*].score")
            .value(hasItem(DEFAULT_SCORE.doubleValue()))
            .jsonPath("$.[*].technicalStatus")
            .value(hasItem(DEFAULT_TECHNICAL_STATUS))
            .jsonPath("$.[*].notes")
            .value(hasItem(DEFAULT_NOTES))
            .jsonPath("$.[*].editBy")
            .value(hasItem(DEFAULT_EDIT_BY));
    }

    @Test
    void getTechnicalInterview() {
        // Initialize the database
        technicalInterviewRepository.save(technicalInterview).block();

        // Get the technicalInterview
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, technicalInterview.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(technicalInterview.getId().intValue()))
            .jsonPath("$.date")
            .value(is(DEFAULT_DATE.toString()))
            .jsonPath("$.score")
            .value(is(DEFAULT_SCORE.doubleValue()))
            .jsonPath("$.technicalStatus")
            .value(is(DEFAULT_TECHNICAL_STATUS))
            .jsonPath("$.notes")
            .value(is(DEFAULT_NOTES))
            .jsonPath("$.editBy")
            .value(is(DEFAULT_EDIT_BY));
    }

    @Test
    void getNonExistingTechnicalInterview() {
        // Get the technicalInterview
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTechnicalInterview() throws Exception {
        // Initialize the database
        technicalInterviewRepository.save(technicalInterview).block();

        int databaseSizeBeforeUpdate = technicalInterviewRepository.findAll().collectList().block().size();

        // Update the technicalInterview
        TechnicalInterview updatedTechnicalInterview = technicalInterviewRepository.findById(technicalInterview.getId()).block();
        updatedTechnicalInterview
            .date(UPDATED_DATE)
            .score(UPDATED_SCORE)
            .technicalStatus(UPDATED_TECHNICAL_STATUS)
            .notes(UPDATED_NOTES)
            .editBy(UPDATED_EDIT_BY);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTechnicalInterview.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedTechnicalInterview))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TechnicalInterview in the database
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeUpdate);
        TechnicalInterview testTechnicalInterview = technicalInterviewList.get(technicalInterviewList.size() - 1);
        assertThat(testTechnicalInterview.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTechnicalInterview.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testTechnicalInterview.getTechnicalStatus()).isEqualTo(UPDATED_TECHNICAL_STATUS);
        assertThat(testTechnicalInterview.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testTechnicalInterview.getEditBy()).isEqualTo(UPDATED_EDIT_BY);
    }

    @Test
    void putNonExistingTechnicalInterview() throws Exception {
        int databaseSizeBeforeUpdate = technicalInterviewRepository.findAll().collectList().block().size();
        technicalInterview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, technicalInterview.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicalInterview))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TechnicalInterview in the database
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTechnicalInterview() throws Exception {
        int databaseSizeBeforeUpdate = technicalInterviewRepository.findAll().collectList().block().size();
        technicalInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicalInterview))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TechnicalInterview in the database
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTechnicalInterview() throws Exception {
        int databaseSizeBeforeUpdate = technicalInterviewRepository.findAll().collectList().block().size();
        technicalInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicalInterview))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TechnicalInterview in the database
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTechnicalInterviewWithPatch() throws Exception {
        // Initialize the database
        technicalInterviewRepository.save(technicalInterview).block();

        int databaseSizeBeforeUpdate = technicalInterviewRepository.findAll().collectList().block().size();

        // Update the technicalInterview using partial update
        TechnicalInterview partialUpdatedTechnicalInterview = new TechnicalInterview();
        partialUpdatedTechnicalInterview.setId(technicalInterview.getId());

        partialUpdatedTechnicalInterview.score(UPDATED_SCORE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTechnicalInterview.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTechnicalInterview))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TechnicalInterview in the database
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeUpdate);
        TechnicalInterview testTechnicalInterview = technicalInterviewList.get(technicalInterviewList.size() - 1);
        assertThat(testTechnicalInterview.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTechnicalInterview.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testTechnicalInterview.getTechnicalStatus()).isEqualTo(DEFAULT_TECHNICAL_STATUS);
        assertThat(testTechnicalInterview.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testTechnicalInterview.getEditBy()).isEqualTo(DEFAULT_EDIT_BY);
    }

    @Test
    void fullUpdateTechnicalInterviewWithPatch() throws Exception {
        // Initialize the database
        technicalInterviewRepository.save(technicalInterview).block();

        int databaseSizeBeforeUpdate = technicalInterviewRepository.findAll().collectList().block().size();

        // Update the technicalInterview using partial update
        TechnicalInterview partialUpdatedTechnicalInterview = new TechnicalInterview();
        partialUpdatedTechnicalInterview.setId(technicalInterview.getId());

        partialUpdatedTechnicalInterview
            .date(UPDATED_DATE)
            .score(UPDATED_SCORE)
            .technicalStatus(UPDATED_TECHNICAL_STATUS)
            .notes(UPDATED_NOTES)
            .editBy(UPDATED_EDIT_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTechnicalInterview.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTechnicalInterview))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TechnicalInterview in the database
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeUpdate);
        TechnicalInterview testTechnicalInterview = technicalInterviewList.get(technicalInterviewList.size() - 1);
        assertThat(testTechnicalInterview.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTechnicalInterview.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testTechnicalInterview.getTechnicalStatus()).isEqualTo(UPDATED_TECHNICAL_STATUS);
        assertThat(testTechnicalInterview.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testTechnicalInterview.getEditBy()).isEqualTo(UPDATED_EDIT_BY);
    }

    @Test
    void patchNonExistingTechnicalInterview() throws Exception {
        int databaseSizeBeforeUpdate = technicalInterviewRepository.findAll().collectList().block().size();
        technicalInterview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, technicalInterview.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicalInterview))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TechnicalInterview in the database
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTechnicalInterview() throws Exception {
        int databaseSizeBeforeUpdate = technicalInterviewRepository.findAll().collectList().block().size();
        technicalInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicalInterview))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TechnicalInterview in the database
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTechnicalInterview() throws Exception {
        int databaseSizeBeforeUpdate = technicalInterviewRepository.findAll().collectList().block().size();
        technicalInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(technicalInterview))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TechnicalInterview in the database
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTechnicalInterview() {
        // Initialize the database
        technicalInterviewRepository.save(technicalInterview).block();

        int databaseSizeBeforeDelete = technicalInterviewRepository.findAll().collectList().block().size();

        // Delete the technicalInterview
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, technicalInterview.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TechnicalInterview> technicalInterviewList = technicalInterviewRepository.findAll().collectList().block();
        assertThat(technicalInterviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
