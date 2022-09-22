package br.com.hcp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.hcp.IntegrationTest;
import br.com.hcp.domain.LocationUser;
import br.com.hcp.repository.LocationUserRepository;
import br.com.hcp.service.LocationUserService;
import br.com.hcp.service.dto.LocationUserDTO;
import br.com.hcp.service.mapper.LocationUserMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LocationUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LocationUserResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/location-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationUserRepository locationUserRepository;

    @Mock
    private LocationUserRepository locationUserRepositoryMock;

    @Autowired
    private LocationUserMapper locationUserMapper;

    @Mock
    private LocationUserService locationUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationUserMockMvc;

    private LocationUser locationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationUser createEntity(EntityManager em) {
        LocationUser locationUser = new LocationUser().login(DEFAULT_LOGIN);
        return locationUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationUser createUpdatedEntity(EntityManager em) {
        LocationUser locationUser = new LocationUser().login(UPDATED_LOGIN);
        return locationUser;
    }

    @BeforeEach
    public void initTest() {
        locationUser = createEntity(em);
    }

    @Test
    @Transactional
    void createLocationUser() throws Exception {
        int databaseSizeBeforeCreate = locationUserRepository.findAll().size();
        // Create the LocationUser
        LocationUserDTO locationUserDTO = locationUserMapper.toDto(locationUser);
        restLocationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LocationUser in the database
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeCreate + 1);
        LocationUser testLocationUser = locationUserList.get(locationUserList.size() - 1);
        assertThat(testLocationUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
    }

    @Test
    @Transactional
    void createLocationUserWithExistingId() throws Exception {
        // Create the LocationUser with an existing ID
        locationUser.setId(1L);
        LocationUserDTO locationUserDTO = locationUserMapper.toDto(locationUser);

        int databaseSizeBeforeCreate = locationUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationUser in the database
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocationUsers() throws Exception {
        // Initialize the database
        locationUserRepository.saveAndFlush(locationUser);

        // Get all the locationUserList
        restLocationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocationUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(locationUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(locationUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocationUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(locationUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocationUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(locationUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLocationUser() throws Exception {
        // Initialize the database
        locationUserRepository.saveAndFlush(locationUser);

        // Get the locationUser
        restLocationUserMockMvc
            .perform(get(ENTITY_API_URL_ID, locationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationUser.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingLocationUser() throws Exception {
        // Get the locationUser
        restLocationUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocationUser() throws Exception {
        // Initialize the database
        locationUserRepository.saveAndFlush(locationUser);

        int databaseSizeBeforeUpdate = locationUserRepository.findAll().size();

        // Update the locationUser
        LocationUser updatedLocationUser = locationUserRepository.findById(locationUser.getId()).get();
        // Disconnect from session so that the updates on updatedLocationUser are not directly saved in db
        em.detach(updatedLocationUser);
        updatedLocationUser.login(UPDATED_LOGIN);
        LocationUserDTO locationUserDTO = locationUserMapper.toDto(updatedLocationUser);

        restLocationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the LocationUser in the database
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeUpdate);
        LocationUser testLocationUser = locationUserList.get(locationUserList.size() - 1);
        assertThat(testLocationUser.getLogin()).isEqualTo(UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingLocationUser() throws Exception {
        int databaseSizeBeforeUpdate = locationUserRepository.findAll().size();
        locationUser.setId(count.incrementAndGet());

        // Create the LocationUser
        LocationUserDTO locationUserDTO = locationUserMapper.toDto(locationUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationUser in the database
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationUser() throws Exception {
        int databaseSizeBeforeUpdate = locationUserRepository.findAll().size();
        locationUser.setId(count.incrementAndGet());

        // Create the LocationUser
        LocationUserDTO locationUserDTO = locationUserMapper.toDto(locationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationUser in the database
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationUser() throws Exception {
        int databaseSizeBeforeUpdate = locationUserRepository.findAll().size();
        locationUser.setId(count.incrementAndGet());

        // Create the LocationUser
        LocationUserDTO locationUserDTO = locationUserMapper.toDto(locationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationUserMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationUser in the database
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationUserWithPatch() throws Exception {
        // Initialize the database
        locationUserRepository.saveAndFlush(locationUser);

        int databaseSizeBeforeUpdate = locationUserRepository.findAll().size();

        // Update the locationUser using partial update
        LocationUser partialUpdatedLocationUser = new LocationUser();
        partialUpdatedLocationUser.setId(locationUser.getId());

        restLocationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationUser))
            )
            .andExpect(status().isOk());

        // Validate the LocationUser in the database
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeUpdate);
        LocationUser testLocationUser = locationUserList.get(locationUserList.size() - 1);
        assertThat(testLocationUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateLocationUserWithPatch() throws Exception {
        // Initialize the database
        locationUserRepository.saveAndFlush(locationUser);

        int databaseSizeBeforeUpdate = locationUserRepository.findAll().size();

        // Update the locationUser using partial update
        LocationUser partialUpdatedLocationUser = new LocationUser();
        partialUpdatedLocationUser.setId(locationUser.getId());

        partialUpdatedLocationUser.login(UPDATED_LOGIN);

        restLocationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationUser))
            )
            .andExpect(status().isOk());

        // Validate the LocationUser in the database
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeUpdate);
        LocationUser testLocationUser = locationUserList.get(locationUserList.size() - 1);
        assertThat(testLocationUser.getLogin()).isEqualTo(UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingLocationUser() throws Exception {
        int databaseSizeBeforeUpdate = locationUserRepository.findAll().size();
        locationUser.setId(count.incrementAndGet());

        // Create the LocationUser
        LocationUserDTO locationUserDTO = locationUserMapper.toDto(locationUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationUser in the database
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationUser() throws Exception {
        int databaseSizeBeforeUpdate = locationUserRepository.findAll().size();
        locationUser.setId(count.incrementAndGet());

        // Create the LocationUser
        LocationUserDTO locationUserDTO = locationUserMapper.toDto(locationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationUser in the database
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationUser() throws Exception {
        int databaseSizeBeforeUpdate = locationUserRepository.findAll().size();
        locationUser.setId(count.incrementAndGet());

        // Create the LocationUser
        LocationUserDTO locationUserDTO = locationUserMapper.toDto(locationUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationUser in the database
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocationUser() throws Exception {
        // Initialize the database
        locationUserRepository.saveAndFlush(locationUser);

        int databaseSizeBeforeDelete = locationUserRepository.findAll().size();

        // Delete the locationUser
        restLocationUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocationUser> locationUserList = locationUserRepository.findAll();
        assertThat(locationUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
