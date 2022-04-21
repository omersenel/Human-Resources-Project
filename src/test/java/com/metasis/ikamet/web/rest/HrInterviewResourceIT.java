package com.metasis.ikamet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.metasis.ikamet.IntegrationTest;
import com.metasis.ikamet.domain.HrInterview;
import com.metasis.ikamet.repository.HrInterviewRepository;
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
 * Integration tests for the {@link HrInterviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class HrInterviewResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_SCORE = 1D;
    private static final Double UPDATED_SCORE = 2D;

    private static final String DEFAULT_IK_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_IK_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_EDIT_BY = "AAAAAAAAAA";
    private static final String UPDATED_EDIT_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hr-interviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HrInterviewRepository hrInterviewRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HrInterview hrInterview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HrInterview createEntity(EntityManager em) {
        HrInterview hrInterview = new HrInterview()
            .date(DEFAULT_DATE)
            .score(DEFAULT_SCORE)
            .ikStatus(DEFAULT_IK_STATUS)
            .notes(DEFAULT_NOTES)
            .editBy(DEFAULT_EDIT_BY);
        return hrInterview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HrInterview createUpdatedEntity(EntityManager em) {
        HrInterview hrInterview = new HrInterview()
            .date(UPDATED_DATE)
            .score(UPDATED_SCORE)
            .ikStatus(UPDATED_IK_STATUS)
            .notes(UPDATED_NOTES)
            .editBy(UPDATED_EDIT_BY);
        return hrInterview;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HrInterview.class).block();
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
        hrInterview = createEntity(em);
    }

    @Test
    void createHrInterview() throws Exception {
        int databaseSizeBeforeCreate = hrInterviewRepository.findAll().collectList().block().size();
        // Create the HrInterview
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hrInterview))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HrInterview in the database
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeCreate + 1);
        HrInterview testHrInterview = hrInterviewList.get(hrInterviewList.size() - 1);
        assertThat(testHrInterview.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testHrInterview.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testHrInterview.getIkStatus()).isEqualTo(DEFAULT_IK_STATUS);
        assertThat(testHrInterview.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testHrInterview.getEditBy()).isEqualTo(DEFAULT_EDIT_BY);
    }

    @Test
    void createHrInterviewWithExistingId() throws Exception {
        // Create the HrInterview with an existing ID
        hrInterview.setId(1L);

        int databaseSizeBeforeCreate = hrInterviewRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hrInterview))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HrInterview in the database
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllHrInterviewsAsStream() {
        // Initialize the database
        hrInterviewRepository.save(hrInterview).block();

        List<HrInterview> hrInterviewList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(HrInterview.class)
            .getResponseBody()
            .filter(hrInterview::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(hrInterviewList).isNotNull();
        assertThat(hrInterviewList).hasSize(1);
        HrInterview testHrInterview = hrInterviewList.get(0);
        assertThat(testHrInterview.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testHrInterview.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testHrInterview.getIkStatus()).isEqualTo(DEFAULT_IK_STATUS);
        assertThat(testHrInterview.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testHrInterview.getEditBy()).isEqualTo(DEFAULT_EDIT_BY);
    }

    @Test
    void getAllHrInterviews() {
        // Initialize the database
        hrInterviewRepository.save(hrInterview).block();

        // Get all the hrInterviewList
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
            .value(hasItem(hrInterview.getId().intValue()))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE.toString()))
            .jsonPath("$.[*].score")
            .value(hasItem(DEFAULT_SCORE.doubleValue()))
            .jsonPath("$.[*].ikStatus")
            .value(hasItem(DEFAULT_IK_STATUS))
            .jsonPath("$.[*].notes")
            .value(hasItem(DEFAULT_NOTES))
            .jsonPath("$.[*].editBy")
            .value(hasItem(DEFAULT_EDIT_BY));
    }

    @Test
    void getHrInterview() {
        // Initialize the database
        hrInterviewRepository.save(hrInterview).block();

        // Get the hrInterview
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, hrInterview.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(hrInterview.getId().intValue()))
            .jsonPath("$.date")
            .value(is(DEFAULT_DATE.toString()))
            .jsonPath("$.score")
            .value(is(DEFAULT_SCORE.doubleValue()))
            .jsonPath("$.ikStatus")
            .value(is(DEFAULT_IK_STATUS))
            .jsonPath("$.notes")
            .value(is(DEFAULT_NOTES))
            .jsonPath("$.editBy")
            .value(is(DEFAULT_EDIT_BY));
    }

    @Test
    void getNonExistingHrInterview() {
        // Get the hrInterview
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewHrInterview() throws Exception {
        // Initialize the database
        hrInterviewRepository.save(hrInterview).block();

        int databaseSizeBeforeUpdate = hrInterviewRepository.findAll().collectList().block().size();

        // Update the hrInterview
        HrInterview updatedHrInterview = hrInterviewRepository.findById(hrInterview.getId()).block();
        updatedHrInterview.date(UPDATED_DATE).score(UPDATED_SCORE).ikStatus(UPDATED_IK_STATUS).notes(UPDATED_NOTES).editBy(UPDATED_EDIT_BY);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedHrInterview.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedHrInterview))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HrInterview in the database
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeUpdate);
        HrInterview testHrInterview = hrInterviewList.get(hrInterviewList.size() - 1);
        assertThat(testHrInterview.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testHrInterview.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testHrInterview.getIkStatus()).isEqualTo(UPDATED_IK_STATUS);
        assertThat(testHrInterview.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testHrInterview.getEditBy()).isEqualTo(UPDATED_EDIT_BY);
    }

    @Test
    void putNonExistingHrInterview() throws Exception {
        int databaseSizeBeforeUpdate = hrInterviewRepository.findAll().collectList().block().size();
        hrInterview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, hrInterview.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hrInterview))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HrInterview in the database
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHrInterview() throws Exception {
        int databaseSizeBeforeUpdate = hrInterviewRepository.findAll().collectList().block().size();
        hrInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hrInterview))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HrInterview in the database
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHrInterview() throws Exception {
        int databaseSizeBeforeUpdate = hrInterviewRepository.findAll().collectList().block().size();
        hrInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hrInterview))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HrInterview in the database
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHrInterviewWithPatch() throws Exception {
        // Initialize the database
        hrInterviewRepository.save(hrInterview).block();

        int databaseSizeBeforeUpdate = hrInterviewRepository.findAll().collectList().block().size();

        // Update the hrInterview using partial update
        HrInterview partialUpdatedHrInterview = new HrInterview();
        partialUpdatedHrInterview.setId(hrInterview.getId());

        partialUpdatedHrInterview.date(UPDATED_DATE).notes(UPDATED_NOTES).editBy(UPDATED_EDIT_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHrInterview.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHrInterview))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HrInterview in the database
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeUpdate);
        HrInterview testHrInterview = hrInterviewList.get(hrInterviewList.size() - 1);
        assertThat(testHrInterview.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testHrInterview.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testHrInterview.getIkStatus()).isEqualTo(DEFAULT_IK_STATUS);
        assertThat(testHrInterview.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testHrInterview.getEditBy()).isEqualTo(UPDATED_EDIT_BY);
    }

    @Test
    void fullUpdateHrInterviewWithPatch() throws Exception {
        // Initialize the database
        hrInterviewRepository.save(hrInterview).block();

        int databaseSizeBeforeUpdate = hrInterviewRepository.findAll().collectList().block().size();

        // Update the hrInterview using partial update
        HrInterview partialUpdatedHrInterview = new HrInterview();
        partialUpdatedHrInterview.setId(hrInterview.getId());

        partialUpdatedHrInterview
            .date(UPDATED_DATE)
            .score(UPDATED_SCORE)
            .ikStatus(UPDATED_IK_STATUS)
            .notes(UPDATED_NOTES)
            .editBy(UPDATED_EDIT_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHrInterview.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHrInterview))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HrInterview in the database
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeUpdate);
        HrInterview testHrInterview = hrInterviewList.get(hrInterviewList.size() - 1);
        assertThat(testHrInterview.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testHrInterview.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testHrInterview.getIkStatus()).isEqualTo(UPDATED_IK_STATUS);
        assertThat(testHrInterview.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testHrInterview.getEditBy()).isEqualTo(UPDATED_EDIT_BY);
    }

    @Test
    void patchNonExistingHrInterview() throws Exception {
        int databaseSizeBeforeUpdate = hrInterviewRepository.findAll().collectList().block().size();
        hrInterview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, hrInterview.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(hrInterview))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HrInterview in the database
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHrInterview() throws Exception {
        int databaseSizeBeforeUpdate = hrInterviewRepository.findAll().collectList().block().size();
        hrInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(hrInterview))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HrInterview in the database
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHrInterview() throws Exception {
        int databaseSizeBeforeUpdate = hrInterviewRepository.findAll().collectList().block().size();
        hrInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(hrInterview))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HrInterview in the database
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHrInterview() {
        // Initialize the database
        hrInterviewRepository.save(hrInterview).block();

        int databaseSizeBeforeDelete = hrInterviewRepository.findAll().collectList().block().size();

        // Delete the hrInterview
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, hrInterview.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HrInterview> hrInterviewList = hrInterviewRepository.findAll().collectList().block();
        assertThat(hrInterviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
