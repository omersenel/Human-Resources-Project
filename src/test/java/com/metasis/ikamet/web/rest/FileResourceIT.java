package com.metasis.ikamet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.metasis.ikamet.IntegrationTest;
import com.metasis.ikamet.domain.File;
import com.metasis.ikamet.repository.FileRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class FileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private File file;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createEntity(EntityManager em) {
        File file = new File().name(DEFAULT_NAME).data(DEFAULT_DATA).dataContentType(DEFAULT_DATA_CONTENT_TYPE);
        return file;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createUpdatedEntity(EntityManager em) {
        File file = new File().name(UPDATED_NAME).data(UPDATED_DATA).dataContentType(UPDATED_DATA_CONTENT_TYPE);
        return file;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(File.class).block();
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
        file = createEntity(em);
    }

    @Test
    void createFile() throws Exception {
        int databaseSizeBeforeCreate = fileRepository.findAll().collectList().block().size();
        // Create the File
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(file))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeCreate + 1);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFile.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testFile.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
    }

    @Test
    void createFileWithExistingId() throws Exception {
        // Create the File with an existing ID
        file.setId(1L);

        int databaseSizeBeforeCreate = fileRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(file))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFilesAsStream() {
        // Initialize the database
        fileRepository.save(file).block();

        List<File> fileList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(File.class)
            .getResponseBody()
            .filter(file::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(fileList).isNotNull();
        assertThat(fileList).hasSize(1);
        File testFile = fileList.get(0);
        assertThat(testFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFile.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testFile.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
    }

    @Test
    void getAllFiles() {
        // Initialize the database
        fileRepository.save(file).block();

        // Get all the fileList
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
            .value(hasItem(file.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].dataContentType")
            .value(hasItem(DEFAULT_DATA_CONTENT_TYPE))
            .jsonPath("$.[*].data")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA)));
    }

    @Test
    void getFile() {
        // Initialize the database
        fileRepository.save(file).block();

        // Get the file
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, file.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(file.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.dataContentType")
            .value(is(DEFAULT_DATA_CONTENT_TYPE))
            .jsonPath("$.data")
            .value(is(Base64Utils.encodeToString(DEFAULT_DATA)));
    }

    @Test
    void getNonExistingFile() {
        // Get the file
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFile() throws Exception {
        // Initialize the database
        fileRepository.save(file).block();

        int databaseSizeBeforeUpdate = fileRepository.findAll().collectList().block().size();

        // Update the file
        File updatedFile = fileRepository.findById(file.getId()).block();
        updatedFile.name(UPDATED_NAME).data(UPDATED_DATA).dataContentType(UPDATED_DATA_CONTENT_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFile.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testFile.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
    }

    @Test
    void putNonExistingFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().collectList().block().size();
        file.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, file.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(file))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().collectList().block().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(file))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().collectList().block().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(file))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFileWithPatch() throws Exception {
        // Initialize the database
        fileRepository.save(file).block();

        int databaseSizeBeforeUpdate = fileRepository.findAll().collectList().block().size();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFile.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testFile.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
    }

    @Test
    void fullUpdateFileWithPatch() throws Exception {
        // Initialize the database
        fileRepository.save(file).block();

        int databaseSizeBeforeUpdate = fileRepository.findAll().collectList().block().size();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile.name(UPDATED_NAME).data(UPDATED_DATA).dataContentType(UPDATED_DATA_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testFile.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
    }

    @Test
    void patchNonExistingFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().collectList().block().size();
        file.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, file.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(file))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().collectList().block().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(file))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().collectList().block().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(file))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFile() {
        // Initialize the database
        fileRepository.save(file).block();

        int databaseSizeBeforeDelete = fileRepository.findAll().collectList().block().size();

        // Delete the file
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, file.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<File> fileList = fileRepository.findAll().collectList().block();
        assertThat(fileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
