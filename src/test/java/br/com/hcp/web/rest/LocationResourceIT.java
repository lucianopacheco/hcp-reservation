//package br.com.hcp.web.rest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import br.com.hcp.IntegrationTest;
//import br.com.hcp.domain.Location;
//import br.com.hcp.domain.enumeration.LocationType;
//import br.com.hcp.repository.LocationRepository;
//import br.com.hcp.service.dto.LocationDTO;
//import br.com.hcp.service.mapper.LocationMapper;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//import javax.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for the {@link LocationResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class LocationResourceIT {
//
//    private static final String DEFAULT_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_NAME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_ZIPCODE = "AAAAAAAAAA";
//    private static final String UPDATED_ZIPCODE = "BBBBBBBBBB";
//
//    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
//    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";
//
//    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
//    private static final String UPDATED_NUMBER = "BBBBBBBBBB";
//
//    private static final String DEFAULT_CITY = "AAAAAAAAAA";
//    private static final String UPDATED_CITY = "BBBBBBBBBB";
//
//    private static final String DEFAULT_STATE = "AAAAAAAAAA";
//    private static final String UPDATED_STATE = "BBBBBBBBBB";
//
//    private static final LocationType DEFAULT_TYPE = LocationType.HOME;
//    private static final LocationType UPDATED_TYPE = LocationType.LOCATION;
//
//    private static final String ENTITY_API_URL = "/api/locations";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private LocationRepository locationRepository;
//
//    @Autowired
//    private LocationMapper locationMapper;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restLocationMockMvc;
//
//    private Location location;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Location createEntity(EntityManager em) {
//        Location location = new Location()
//            .name(DEFAULT_NAME)
//            .zipcode(DEFAULT_ZIPCODE)
//            .address(DEFAULT_ADDRESS)
//            .number(DEFAULT_NUMBER)
//            .city(DEFAULT_CITY)
//            .state(DEFAULT_STATE)
//            .type(DEFAULT_TYPE);
//        return location;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Location createUpdatedEntity(EntityManager em) {
//        Location location = new Location()
//            .name(UPDATED_NAME)
//            .zipcode(UPDATED_ZIPCODE)
//            .address(UPDATED_ADDRESS)
//            .number(UPDATED_NUMBER)
//            .city(UPDATED_CITY)
//            .state(UPDATED_STATE)
//            .type(UPDATED_TYPE);
//        return location;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        location = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createLocation() throws Exception {
//        int databaseSizeBeforeCreate = locationRepository.findAll().size();
//        // Create the Location
//        LocationDTO locationDTO = locationMapper.toDto(location);
//        restLocationMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
//            .andExpect(status().isCreated());
//
//        // Validate the Location in the database
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
//        Location testLocation = locationList.get(locationList.size() - 1);
//        assertThat(testLocation.getName()).isEqualTo(DEFAULT_NAME);
//        assertThat(testLocation.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
//        assertThat(testLocation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
//        assertThat(testLocation.getNumber()).isEqualTo(DEFAULT_NUMBER);
//        assertThat(testLocation.getCity()).isEqualTo(DEFAULT_CITY);
//        assertThat(testLocation.getState()).isEqualTo(DEFAULT_STATE);
//        assertThat(testLocation.getType()).isEqualTo(DEFAULT_TYPE);
//    }
//
//    @Test
//    @Transactional
//    void createLocationWithExistingId() throws Exception {
//        // Create the Location with an existing ID
//        location.setId(1L);
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        int databaseSizeBeforeCreate = locationRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restLocationMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Location in the database
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void checkNameIsRequired() throws Exception {
//        int databaseSizeBeforeTest = locationRepository.findAll().size();
//        // set the field null
//        location.setName(null);
//
//        // Create the Location, which fails.
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        restLocationMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
//            .andExpect(status().isBadRequest());
//
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkZipcodeIsRequired() throws Exception {
//        int databaseSizeBeforeTest = locationRepository.findAll().size();
//        // set the field null
//        location.setZipcode(null);
//
//        // Create the Location, which fails.
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        restLocationMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
//            .andExpect(status().isBadRequest());
//
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkNumberIsRequired() throws Exception {
//        int databaseSizeBeforeTest = locationRepository.findAll().size();
//        // set the field null
//        location.setNumber(null);
//
//        // Create the Location, which fails.
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        restLocationMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
//            .andExpect(status().isBadRequest());
//
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkCityIsRequired() throws Exception {
//        int databaseSizeBeforeTest = locationRepository.findAll().size();
//        // set the field null
//        location.setCity(null);
//
//        // Create the Location, which fails.
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        restLocationMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
//            .andExpect(status().isBadRequest());
//
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkStateIsRequired() throws Exception {
//        int databaseSizeBeforeTest = locationRepository.findAll().size();
//        // set the field null
//        location.setState(null);
//
//        // Create the Location, which fails.
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        restLocationMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
//            .andExpect(status().isBadRequest());
//
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void getAllLocations() throws Exception {
//        // Initialize the database
//        locationRepository.saveAndFlush(location);
//
//        // Get all the locationList
//        restLocationMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
//            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
//            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)))
//            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
//            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
//            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
//            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
//            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
//    }
//
//    @Test
//    @Transactional
//    void getLocation() throws Exception {
//        // Initialize the database
//        locationRepository.saveAndFlush(location);
//
//        // Get the location
//        restLocationMockMvc
//            .perform(get(ENTITY_API_URL_ID, location.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
//            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
//            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE))
//            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
//            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
//            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
//            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
//            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingLocation() throws Exception {
//        // Get the location
//        restLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewLocation() throws Exception {
//        // Initialize the database
//        locationRepository.saveAndFlush(location);
//
//        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
//
//        // Update the location
//        Location updatedLocation = locationRepository.findById(location.getId()).get();
//        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
//        em.detach(updatedLocation);
//        updatedLocation
//            .name(UPDATED_NAME)
//            .zipcode(UPDATED_ZIPCODE)
//            .address(UPDATED_ADDRESS)
//            .number(UPDATED_NUMBER)
//            .city(UPDATED_CITY)
//            .state(UPDATED_STATE)
//            .type(UPDATED_TYPE);
//        LocationDTO locationDTO = locationMapper.toDto(updatedLocation);
//
//        restLocationMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, locationDTO.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Location in the database
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
//        Location testLocation = locationList.get(locationList.size() - 1);
//        assertThat(testLocation.getName()).isEqualTo(UPDATED_NAME);
//        assertThat(testLocation.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
//        assertThat(testLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
//        assertThat(testLocation.getNumber()).isEqualTo(UPDATED_NUMBER);
//        assertThat(testLocation.getCity()).isEqualTo(UPDATED_CITY);
//        assertThat(testLocation.getState()).isEqualTo(UPDATED_STATE);
//        assertThat(testLocation.getType()).isEqualTo(UPDATED_TYPE);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingLocation() throws Exception {
//        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
//        location.setId(count.incrementAndGet());
//
//        // Create the Location
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restLocationMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, locationDTO.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Location in the database
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchLocation() throws Exception {
//        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
//        location.setId(count.incrementAndGet());
//
//        // Create the Location
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restLocationMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Location in the database
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamLocation() throws Exception {
//        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
//        location.setId(count.incrementAndGet());
//
//        // Create the Location
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restLocationMockMvc
//            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationDTO)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Location in the database
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateLocationWithPatch() throws Exception {
//        // Initialize the database
//        locationRepository.saveAndFlush(location);
//
//        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
//
//        // Update the location using partial update
//        Location partialUpdatedLocation = new Location();
//        partialUpdatedLocation.setId(location.getId());
//
//        partialUpdatedLocation.name(UPDATED_NAME).address(UPDATED_ADDRESS).number(UPDATED_NUMBER).state(UPDATED_STATE);
//
//        restLocationMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Location in the database
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
//        Location testLocation = locationList.get(locationList.size() - 1);
//        assertThat(testLocation.getName()).isEqualTo(UPDATED_NAME);
//        assertThat(testLocation.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
//        assertThat(testLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
//        assertThat(testLocation.getNumber()).isEqualTo(UPDATED_NUMBER);
//        assertThat(testLocation.getCity()).isEqualTo(DEFAULT_CITY);
//        assertThat(testLocation.getState()).isEqualTo(UPDATED_STATE);
//        assertThat(testLocation.getType()).isEqualTo(DEFAULT_TYPE);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateLocationWithPatch() throws Exception {
//        // Initialize the database
//        locationRepository.saveAndFlush(location);
//
//        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
//
//        // Update the location using partial update
//        Location partialUpdatedLocation = new Location();
//        partialUpdatedLocation.setId(location.getId());
//
//        partialUpdatedLocation
//            .name(UPDATED_NAME)
//            .zipcode(UPDATED_ZIPCODE)
//            .address(UPDATED_ADDRESS)
//            .number(UPDATED_NUMBER)
//            .city(UPDATED_CITY)
//            .state(UPDATED_STATE)
//            .type(UPDATED_TYPE);
//
//        restLocationMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Location in the database
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
//        Location testLocation = locationList.get(locationList.size() - 1);
//        assertThat(testLocation.getName()).isEqualTo(UPDATED_NAME);
//        assertThat(testLocation.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
//        assertThat(testLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
//        assertThat(testLocation.getNumber()).isEqualTo(UPDATED_NUMBER);
//        assertThat(testLocation.getCity()).isEqualTo(UPDATED_CITY);
//        assertThat(testLocation.getState()).isEqualTo(UPDATED_STATE);
//        assertThat(testLocation.getType()).isEqualTo(UPDATED_TYPE);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingLocation() throws Exception {
//        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
//        location.setId(count.incrementAndGet());
//
//        // Create the Location
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restLocationMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, locationDTO.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Location in the database
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchLocation() throws Exception {
//        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
//        location.setId(count.incrementAndGet());
//
//        // Create the Location
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restLocationMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(locationDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Location in the database
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamLocation() throws Exception {
//        int databaseSizeBeforeUpdate = locationRepository.findAll().size();
//        location.setId(count.incrementAndGet());
//
//        // Create the Location
//        LocationDTO locationDTO = locationMapper.toDto(location);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restLocationMockMvc
//            .perform(
//                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(locationDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Location in the database
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteLocation() throws Exception {
//        // Initialize the database
//        locationRepository.saveAndFlush(location);
//
//        int databaseSizeBeforeDelete = locationRepository.findAll().size();
//
//        // Delete the location
//        restLocationMockMvc
//            .perform(delete(ENTITY_API_URL_ID, location.getId()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<Location> locationList = locationRepository.findAll();
//        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
