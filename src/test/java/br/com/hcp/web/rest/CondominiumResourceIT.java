package br.com.hcp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.hcp.IntegrationTest;
import br.com.hcp.domain.Condominium;
import br.com.hcp.repository.CondominiumRepository;
import br.com.hcp.service.dto.CondominiumDTO;
import br.com.hcp.service.mapper.CondominiumMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CondominiumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CondominiumResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/condominiums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CondominiumRepository condominiumRepository;

    @Autowired
    private CondominiumMapper condominiumMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCondominiumMockMvc;

    private Condominium condominium;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Condominium createEntity(EntityManager em) {
        Condominium condominium = new Condominium().name(DEFAULT_NAME);
        return condominium;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Condominium createUpdatedEntity(EntityManager em) {
        Condominium condominium = new Condominium().name(UPDATED_NAME);
        return condominium;
    }

    @BeforeEach
    public void initTest() {
        condominium = createEntity(em);
    }

    @Test
    @Transactional
    void createCondominium() throws Exception {
        int databaseSizeBeforeCreate = condominiumRepository.findAll().size();
        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);
        restCondominiumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeCreate + 1);
        Condominium testCondominium = condominiumList.get(condominiumList.size() - 1);
        assertThat(testCondominium.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCondominiumWithExistingId() throws Exception {
        // Create the Condominium with an existing ID
        condominium.setId(1L);
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        int databaseSizeBeforeCreate = condominiumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCondominiumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = condominiumRepository.findAll().size();
        // set the field null
        condominium.setName(null);

        // Create the Condominium, which fails.
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        restCondominiumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCondominiums() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        // Get all the condominiumList
        restCondominiumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(condominium.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCondominium() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        // Get the condominium
        restCondominiumMockMvc
            .perform(get(ENTITY_API_URL_ID, condominium.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(condominium.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCondominium() throws Exception {
        // Get the condominium
        restCondominiumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCondominium() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();

        // Update the condominium
        Condominium updatedCondominium = condominiumRepository.findById(condominium.getId()).get();
        // Disconnect from session so that the updates on updatedCondominium are not directly saved in db
        em.detach(updatedCondominium);
        updatedCondominium.name(UPDATED_NAME);
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(updatedCondominium);

        restCondominiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, condominiumDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isOk());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
        Condominium testCondominium = condominiumList.get(condominiumList.size() - 1);
        assertThat(testCondominium.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, condominiumDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(condominiumDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCondominiumWithPatch() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();

        // Update the condominium using partial update
        Condominium partialUpdatedCondominium = new Condominium();
        partialUpdatedCondominium.setId(condominium.getId());

        partialUpdatedCondominium.name(UPDATED_NAME);

        restCondominiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCondominium.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCondominium))
            )
            .andExpect(status().isOk());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
        Condominium testCondominium = condominiumList.get(condominiumList.size() - 1);
        assertThat(testCondominium.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCondominiumWithPatch() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();

        // Update the condominium using partial update
        Condominium partialUpdatedCondominium = new Condominium();
        partialUpdatedCondominium.setId(condominium.getId());

        partialUpdatedCondominium.name(UPDATED_NAME);

        restCondominiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCondominium.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCondominium))
            )
            .andExpect(status().isOk());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
        Condominium testCondominium = condominiumList.get(condominiumList.size() - 1);
        assertThat(testCondominium.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, condominiumDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCondominium() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        int databaseSizeBeforeDelete = condominiumRepository.findAll().size();

        // Delete the condominium
        restCondominiumMockMvc
            .perform(delete(ENTITY_API_URL_ID, condominium.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
