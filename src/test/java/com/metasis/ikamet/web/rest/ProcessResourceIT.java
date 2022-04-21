package com.metasis.ikamet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.metasis.ikamet.IntegrationTest;
import com.metasis.ikamet.domain.Process;
import com.metasis.ikamet.repository.ProcessRepository;
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
 * Integration tests for the {@link ProcessResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ProcessResourceIT {

    private static final Instant DEFAULT_PDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNICAL_INDICATORS = "AAAAAAAAAA";
    private static final String UPDATED_TECHNICAL_INDICATORS = "BBBBBBBBBB";

    private static final String DEFAULT_EXPERIENCE = "AAAAAAAAAA";
    private static final String UPDATED_EXPERIENCE = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final Double DEFAULT_SALARY_EXPECTATION = 1D;
    private static final Double UPDATED_SALARY_EXPECTATION = 2D;

    private static final String DEFAULT_POSSIBLE_ASSIGNMNET = "AAAAAAAAAA";
    private static final String UPDATED_POSSIBLE_ASSIGNMNET = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_LAST_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LAST_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EDIT_BY = "AAAAAAAAAA";
    private static final String UPDATED_EDIT_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/processes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Process process;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Process createEntity(EntityManager em) {
        Process process = new Process()
            .pdate(DEFAULT_PDATE)
            .department(DEFAULT_DEPARTMENT)
            .technicalIndicators(DEFAULT_TECHNICAL_INDICATORS)
            .experience(DEFAULT_EXPERIENCE)
            .position(DEFAULT_POSITION)
            .salaryExpectation(DEFAULT_SALARY_EXPECTATION)
            .possibleAssignmnet(DEFAULT_POSSIBLE_ASSIGNMNET)
            .lastStatus(DEFAULT_LAST_STATUS)
            .lastDescription(DEFAULT_LAST_DESCRIPTION)
            .editBy(DEFAULT_EDIT_BY);
        return process;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Process createUpdatedEntity(EntityManager em) {
        Process process = new Process()
            .pdate(UPDATED_PDATE)
            .department(UPDATED_DEPARTMENT)
            .technicalIndicators(UPDATED_TECHNICAL_INDICATORS)
            .experience(UPDATED_EXPERIENCE)
            .position(UPDATED_POSITION)
            .salaryExpectation(UPDATED_SALARY_EXPECTATION)
            .possibleAssignmnet(UPDATED_POSSIBLE_ASSIGNMNET)
            .lastStatus(UPDATED_LAST_STATUS)
            .lastDescription(UPDATED_LAST_DESCRIPTION)
            .editBy(UPDATED_EDIT_BY);
        return process;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Process.class).block();
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
        process = createEntity(em);
    }

    @Test
    void createProcess() throws Exception {
        int databaseSizeBeforeCreate = processRepository.findAll().collectList().block().size();
        // Create the Process
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(process))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeCreate + 1);
        Process testProcess = processList.get(processList.size() - 1);
        assertThat(testProcess.getPdate()).isEqualTo(DEFAULT_PDATE);
        assertThat(testProcess.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testProcess.getTechnicalIndicators()).isEqualTo(DEFAULT_TECHNICAL_INDICATORS);
        assertThat(testProcess.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
        assertThat(testProcess.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testProcess.getSalaryExpectation()).isEqualTo(DEFAULT_SALARY_EXPECTATION);
        assertThat(testProcess.getPossibleAssignmnet()).isEqualTo(DEFAULT_POSSIBLE_ASSIGNMNET);
        assertThat(testProcess.getLastStatus()).isEqualTo(DEFAULT_LAST_STATUS);
        assertThat(testProcess.getLastDescription()).isEqualTo(DEFAULT_LAST_DESCRIPTION);
        assertThat(testProcess.getEditBy()).isEqualTo(DEFAULT_EDIT_BY);
    }

    @Test
    void createProcessWithExistingId() throws Exception {
        // Create the Process with an existing ID
        process.setId(1L);

        int databaseSizeBeforeCreate = processRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(process))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllProcessesAsStream() {
        // Initialize the database
        processRepository.save(process).block();

        List<Process> processList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Process.class)
            .getResponseBody()
            .filter(process::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(processList).isNotNull();
        assertThat(processList).hasSize(1);
        Process testProcess = processList.get(0);
        assertThat(testProcess.getPdate()).isEqualTo(DEFAULT_PDATE);
        assertThat(testProcess.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testProcess.getTechnicalIndicators()).isEqualTo(DEFAULT_TECHNICAL_INDICATORS);
        assertThat(testProcess.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
        assertThat(testProcess.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testProcess.getSalaryExpectation()).isEqualTo(DEFAULT_SALARY_EXPECTATION);
        assertThat(testProcess.getPossibleAssignmnet()).isEqualTo(DEFAULT_POSSIBLE_ASSIGNMNET);
        assertThat(testProcess.getLastStatus()).isEqualTo(DEFAULT_LAST_STATUS);
        assertThat(testProcess.getLastDescription()).isEqualTo(DEFAULT_LAST_DESCRIPTION);
        assertThat(testProcess.getEditBy()).isEqualTo(DEFAULT_EDIT_BY);
    }

    @Test
    void getAllProcesses() {
        // Initialize the database
        processRepository.save(process).block();

        // Get all the processList
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
            .value(hasItem(process.getId().intValue()))
            .jsonPath("$.[*].pdate")
            .value(hasItem(DEFAULT_PDATE.toString()))
            .jsonPath("$.[*].department")
            .value(hasItem(DEFAULT_DEPARTMENT))
            .jsonPath("$.[*].technicalIndicators")
            .value(hasItem(DEFAULT_TECHNICAL_INDICATORS))
            .jsonPath("$.[*].experience")
            .value(hasItem(DEFAULT_EXPERIENCE))
            .jsonPath("$.[*].position")
            .value(hasItem(DEFAULT_POSITION))
            .jsonPath("$.[*].salaryExpectation")
            .value(hasItem(DEFAULT_SALARY_EXPECTATION.doubleValue()))
            .jsonPath("$.[*].possibleAssignmnet")
            .value(hasItem(DEFAULT_POSSIBLE_ASSIGNMNET))
            .jsonPath("$.[*].lastStatus")
            .value(hasItem(DEFAULT_LAST_STATUS))
            .jsonPath("$.[*].lastDescription")
            .value(hasItem(DEFAULT_LAST_DESCRIPTION))
            .jsonPath("$.[*].editBy")
            .value(hasItem(DEFAULT_EDIT_BY));
    }

    @Test
    void getProcess() {
        // Initialize the database
        processRepository.save(process).block();

        // Get the process
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, process.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(process.getId().intValue()))
            .jsonPath("$.pdate")
            .value(is(DEFAULT_PDATE.toString()))
            .jsonPath("$.department")
            .value(is(DEFAULT_DEPARTMENT))
            .jsonPath("$.technicalIndicators")
            .value(is(DEFAULT_TECHNICAL_INDICATORS))
            .jsonPath("$.experience")
            .value(is(DEFAULT_EXPERIENCE))
            .jsonPath("$.position")
            .value(is(DEFAULT_POSITION))
            .jsonPath("$.salaryExpectation")
            .value(is(DEFAULT_SALARY_EXPECTATION.doubleValue()))
            .jsonPath("$.possibleAssignmnet")
            .value(is(DEFAULT_POSSIBLE_ASSIGNMNET))
            .jsonPath("$.lastStatus")
            .value(is(DEFAULT_LAST_STATUS))
            .jsonPath("$.lastDescription")
            .value(is(DEFAULT_LAST_DESCRIPTION))
            .jsonPath("$.editBy")
            .value(is(DEFAULT_EDIT_BY));
    }

    @Test
    void getNonExistingProcess() {
        // Get the process
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProcess() throws Exception {
        // Initialize the database
        processRepository.save(process).block();

        int databaseSizeBeforeUpdate = processRepository.findAll().collectList().block().size();

        // Update the process
        Process updatedProcess = processRepository.findById(process.getId()).block();
        updatedProcess
            .pdate(UPDATED_PDATE)
            .department(UPDATED_DEPARTMENT)
            .technicalIndicators(UPDATED_TECHNICAL_INDICATORS)
            .experience(UPDATED_EXPERIENCE)
            .position(UPDATED_POSITION)
            .salaryExpectation(UPDATED_SALARY_EXPECTATION)
            .possibleAssignmnet(UPDATED_POSSIBLE_ASSIGNMNET)
            .lastStatus(UPDATED_LAST_STATUS)
            .lastDescription(UPDATED_LAST_DESCRIPTION)
            .editBy(UPDATED_EDIT_BY);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedProcess.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedProcess))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
        Process testProcess = processList.get(processList.size() - 1);
        assertThat(testProcess.getPdate()).isEqualTo(UPDATED_PDATE);
        assertThat(testProcess.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testProcess.getTechnicalIndicators()).isEqualTo(UPDATED_TECHNICAL_INDICATORS);
        assertThat(testProcess.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testProcess.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testProcess.getSalaryExpectation()).isEqualTo(UPDATED_SALARY_EXPECTATION);
        assertThat(testProcess.getPossibleAssignmnet()).isEqualTo(UPDATED_POSSIBLE_ASSIGNMNET);
        assertThat(testProcess.getLastStatus()).isEqualTo(UPDATED_LAST_STATUS);
        assertThat(testProcess.getLastDescription()).isEqualTo(UPDATED_LAST_DESCRIPTION);
        assertThat(testProcess.getEditBy()).isEqualTo(UPDATED_EDIT_BY);
    }

    @Test
    void putNonExistingProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().collectList().block().size();
        process.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, process.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(process))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().collectList().block().size();
        process.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(process))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().collectList().block().size();
        process.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(process))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProcessWithPatch() throws Exception {
        // Initialize the database
        processRepository.save(process).block();

        int databaseSizeBeforeUpdate = processRepository.findAll().collectList().block().size();

        // Update the process using partial update
        Process partialUpdatedProcess = new Process();
        partialUpdatedProcess.setId(process.getId());

        partialUpdatedProcess.experience(UPDATED_EXPERIENCE).position(UPDATED_POSITION).lastDescription(UPDATED_LAST_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProcess.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProcess))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
        Process testProcess = processList.get(processList.size() - 1);
        assertThat(testProcess.getPdate()).isEqualTo(DEFAULT_PDATE);
        assertThat(testProcess.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testProcess.getTechnicalIndicators()).isEqualTo(DEFAULT_TECHNICAL_INDICATORS);
        assertThat(testProcess.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testProcess.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testProcess.getSalaryExpectation()).isEqualTo(DEFAULT_SALARY_EXPECTATION);
        assertThat(testProcess.getPossibleAssignmnet()).isEqualTo(DEFAULT_POSSIBLE_ASSIGNMNET);
        assertThat(testProcess.getLastStatus()).isEqualTo(DEFAULT_LAST_STATUS);
        assertThat(testProcess.getLastDescription()).isEqualTo(UPDATED_LAST_DESCRIPTION);
        assertThat(testProcess.getEditBy()).isEqualTo(DEFAULT_EDIT_BY);
    }

    @Test
    void fullUpdateProcessWithPatch() throws Exception {
        // Initialize the database
        processRepository.save(process).block();

        int databaseSizeBeforeUpdate = processRepository.findAll().collectList().block().size();

        // Update the process using partial update
        Process partialUpdatedProcess = new Process();
        partialUpdatedProcess.setId(process.getId());

        partialUpdatedProcess
            .pdate(UPDATED_PDATE)
            .department(UPDATED_DEPARTMENT)
            .technicalIndicators(UPDATED_TECHNICAL_INDICATORS)
            .experience(UPDATED_EXPERIENCE)
            .position(UPDATED_POSITION)
            .salaryExpectation(UPDATED_SALARY_EXPECTATION)
            .possibleAssignmnet(UPDATED_POSSIBLE_ASSIGNMNET)
            .lastStatus(UPDATED_LAST_STATUS)
            .lastDescription(UPDATED_LAST_DESCRIPTION)
            .editBy(UPDATED_EDIT_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProcess.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProcess))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
        Process testProcess = processList.get(processList.size() - 1);
        assertThat(testProcess.getPdate()).isEqualTo(UPDATED_PDATE);
        assertThat(testProcess.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testProcess.getTechnicalIndicators()).isEqualTo(UPDATED_TECHNICAL_INDICATORS);
        assertThat(testProcess.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testProcess.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testProcess.getSalaryExpectation()).isEqualTo(UPDATED_SALARY_EXPECTATION);
        assertThat(testProcess.getPossibleAssignmnet()).isEqualTo(UPDATED_POSSIBLE_ASSIGNMNET);
        assertThat(testProcess.getLastStatus()).isEqualTo(UPDATED_LAST_STATUS);
        assertThat(testProcess.getLastDescription()).isEqualTo(UPDATED_LAST_DESCRIPTION);
        assertThat(testProcess.getEditBy()).isEqualTo(UPDATED_EDIT_BY);
    }

    @Test
    void patchNonExistingProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().collectList().block().size();
        process.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, process.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(process))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().collectList().block().size();
        process.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(process))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().collectList().block().size();
        process.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(process))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProcess() {
        // Initialize the database
        processRepository.save(process).block();

        int databaseSizeBeforeDelete = processRepository.findAll().collectList().block().size();

        // Delete the process
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, process.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Process> processList = processRepository.findAll().collectList().block();
        assertThat(processList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
