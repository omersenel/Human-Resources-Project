package com.metasis.ikamet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.metasis.ikamet.IntegrationTest;
import com.metasis.ikamet.domain.Candidate;
import com.metasis.ikamet.repository.CandidateRepository;
import com.metasis.ikamet.service.EntityManager;
import java.time.Duration;
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
 * Integration tests for the {@link CandidateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class CandidateResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIVERSITY = "AAAAAAAAAA";
    private static final String UPDATED_UNIVERSITY = "BBBBBBBBBB";

    private static final String DEFAULT_GRADUATION_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_GRADUATION_YEAR = "BBBBBBBBBB";

    private static final Double DEFAULT_GPA = 1D;
    private static final Double UPDATED_GPA = 2D;

    private static final String DEFAULT_EDIT_BY = "AAAAAAAAAA";
    private static final String UPDATED_EDIT_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/candidates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Candidate candidate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidate createEntity(EntityManager em) {
        Candidate candidate = new Candidate()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .university(DEFAULT_UNIVERSITY)
            .graduationYear(DEFAULT_GRADUATION_YEAR)
            .gpa(DEFAULT_GPA)
            .editBy(DEFAULT_EDIT_BY);
        return candidate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidate createUpdatedEntity(EntityManager em) {
        Candidate candidate = new Candidate()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .university(UPDATED_UNIVERSITY)
            .graduationYear(UPDATED_GRADUATION_YEAR)
            .gpa(UPDATED_GPA)
            .editBy(UPDATED_EDIT_BY);
        return candidate;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Candidate.class).block();
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
        candidate = createEntity(em);
    }

    @Test
    void createCandidate() throws Exception {
        int databaseSizeBeforeCreate = candidateRepository.findAll().collectList().block().size();
        // Create the Candidate
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(candidate))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeCreate + 1);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCandidate.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCandidate.getUniversity()).isEqualTo(DEFAULT_UNIVERSITY);
        assertThat(testCandidate.getGraduationYear()).isEqualTo(DEFAULT_GRADUATION_YEAR);
        assertThat(testCandidate.getGpa()).isEqualTo(DEFAULT_GPA);
        assertThat(testCandidate.getEditBy()).isEqualTo(DEFAULT_EDIT_BY);
    }

    @Test
    void createCandidateWithExistingId() throws Exception {
        // Create the Candidate with an existing ID
        candidate.setId(1L);

        int databaseSizeBeforeCreate = candidateRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(candidate))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCandidatesAsStream() {
        // Initialize the database
        candidateRepository.save(candidate).block();

        List<Candidate> candidateList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Candidate.class)
            .getResponseBody()
            .filter(candidate::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(candidateList).isNotNull();
        assertThat(candidateList).hasSize(1);
        Candidate testCandidate = candidateList.get(0);
        assertThat(testCandidate.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCandidate.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCandidate.getUniversity()).isEqualTo(DEFAULT_UNIVERSITY);
        assertThat(testCandidate.getGraduationYear()).isEqualTo(DEFAULT_GRADUATION_YEAR);
        assertThat(testCandidate.getGpa()).isEqualTo(DEFAULT_GPA);
        assertThat(testCandidate.getEditBy()).isEqualTo(DEFAULT_EDIT_BY);
    }

    @Test
    void getAllCandidates() {
        // Initialize the database
        candidateRepository.save(candidate).block();

        // Get all the candidateList
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
            .value(hasItem(candidate.getId().intValue()))
            .jsonPath("$.[*].firstName")
            .value(hasItem(DEFAULT_FIRST_NAME))
            .jsonPath("$.[*].lastName")
            .value(hasItem(DEFAULT_LAST_NAME))
            .jsonPath("$.[*].university")
            .value(hasItem(DEFAULT_UNIVERSITY))
            .jsonPath("$.[*].graduationYear")
            .value(hasItem(DEFAULT_GRADUATION_YEAR))
            .jsonPath("$.[*].gpa")
            .value(hasItem(DEFAULT_GPA.doubleValue()))
            .jsonPath("$.[*].editBy")
            .value(hasItem(DEFAULT_EDIT_BY));
    }

    @Test
    void getCandidate() {
        // Initialize the database
        candidateRepository.save(candidate).block();

        // Get the candidate
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, candidate.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(candidate.getId().intValue()))
            .jsonPath("$.firstName")
            .value(is(DEFAULT_FIRST_NAME))
            .jsonPath("$.lastName")
            .value(is(DEFAULT_LAST_NAME))
            .jsonPath("$.university")
            .value(is(DEFAULT_UNIVERSITY))
            .jsonPath("$.graduationYear")
            .value(is(DEFAULT_GRADUATION_YEAR))
            .jsonPath("$.gpa")
            .value(is(DEFAULT_GPA.doubleValue()))
            .jsonPath("$.editBy")
            .value(is(DEFAULT_EDIT_BY));
    }

    @Test
    void getNonExistingCandidate() {
        // Get the candidate
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCandidate() throws Exception {
        // Initialize the database
        candidateRepository.save(candidate).block();

        int databaseSizeBeforeUpdate = candidateRepository.findAll().collectList().block().size();

        // Update the candidate
        Candidate updatedCandidate = candidateRepository.findById(candidate.getId()).block();
        updatedCandidate
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .university(UPDATED_UNIVERSITY)
            .graduationYear(UPDATED_GRADUATION_YEAR)
            .gpa(UPDATED_GPA)
            .editBy(UPDATED_EDIT_BY);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCandidate.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCandidate))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCandidate.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCandidate.getUniversity()).isEqualTo(UPDATED_UNIVERSITY);
        assertThat(testCandidate.getGraduationYear()).isEqualTo(UPDATED_GRADUATION_YEAR);
        assertThat(testCandidate.getGpa()).isEqualTo(UPDATED_GPA);
        assertThat(testCandidate.getEditBy()).isEqualTo(UPDATED_EDIT_BY);
    }

    @Test
    void putNonExistingCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().collectList().block().size();
        candidate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, candidate.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(candidate))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().collectList().block().size();
        candidate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(candidate))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().collectList().block().size();
        candidate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(candidate))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCandidateWithPatch() throws Exception {
        // Initialize the database
        candidateRepository.save(candidate).block();

        int databaseSizeBeforeUpdate = candidateRepository.findAll().collectList().block().size();

        // Update the candidate using partial update
        Candidate partialUpdatedCandidate = new Candidate();
        partialUpdatedCandidate.setId(candidate.getId());

        partialUpdatedCandidate
            .firstName(UPDATED_FIRST_NAME)
            .graduationYear(UPDATED_GRADUATION_YEAR)
            .gpa(UPDATED_GPA)
            .editBy(UPDATED_EDIT_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCandidate.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidate))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCandidate.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCandidate.getUniversity()).isEqualTo(DEFAULT_UNIVERSITY);
        assertThat(testCandidate.getGraduationYear()).isEqualTo(UPDATED_GRADUATION_YEAR);
        assertThat(testCandidate.getGpa()).isEqualTo(UPDATED_GPA);
        assertThat(testCandidate.getEditBy()).isEqualTo(UPDATED_EDIT_BY);
    }

    @Test
    void fullUpdateCandidateWithPatch() throws Exception {
        // Initialize the database
        candidateRepository.save(candidate).block();

        int databaseSizeBeforeUpdate = candidateRepository.findAll().collectList().block().size();

        // Update the candidate using partial update
        Candidate partialUpdatedCandidate = new Candidate();
        partialUpdatedCandidate.setId(candidate.getId());

        partialUpdatedCandidate
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .university(UPDATED_UNIVERSITY)
            .graduationYear(UPDATED_GRADUATION_YEAR)
            .gpa(UPDATED_GPA)
            .editBy(UPDATED_EDIT_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCandidate.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidate))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
        Candidate testCandidate = candidateList.get(candidateList.size() - 1);
        assertThat(testCandidate.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCandidate.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCandidate.getUniversity()).isEqualTo(UPDATED_UNIVERSITY);
        assertThat(testCandidate.getGraduationYear()).isEqualTo(UPDATED_GRADUATION_YEAR);
        assertThat(testCandidate.getGpa()).isEqualTo(UPDATED_GPA);
        assertThat(testCandidate.getEditBy()).isEqualTo(UPDATED_EDIT_BY);
    }

    @Test
    void patchNonExistingCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().collectList().block().size();
        candidate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, candidate.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(candidate))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().collectList().block().size();
        candidate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(candidate))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCandidate() throws Exception {
        int databaseSizeBeforeUpdate = candidateRepository.findAll().collectList().block().size();
        candidate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(candidate))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Candidate in the database
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCandidate() {
        // Initialize the database
        candidateRepository.save(candidate).block();

        int databaseSizeBeforeDelete = candidateRepository.findAll().collectList().block().size();

        // Delete the candidate
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, candidate.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Candidate> candidateList = candidateRepository.findAll().collectList().block();
        assertThat(candidateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
