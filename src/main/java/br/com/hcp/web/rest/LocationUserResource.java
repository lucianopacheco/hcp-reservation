package br.com.hcp.web.rest;

import br.com.hcp.repository.LocationUserRepository;
import br.com.hcp.service.LocationUserService;
import br.com.hcp.service.dto.LocationUserDTO;
import br.com.hcp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.hcp.domain.LocationUser}.
 */
@RestController
@RequestMapping("/api")
public class LocationUserResource {

    private final Logger log = LoggerFactory.getLogger(LocationUserResource.class);

    private static final String ENTITY_NAME = "reservationLocationUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationUserService locationUserService;

    private final LocationUserRepository locationUserRepository;

    public LocationUserResource(LocationUserService locationUserService, LocationUserRepository locationUserRepository) {
        this.locationUserService = locationUserService;
        this.locationUserRepository = locationUserRepository;
    }

    /**
     * {@code POST  /location-users} : Create a new locationUser.
     *
     * @param locationUserDTO the locationUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationUserDTO, or with status {@code 400 (Bad Request)} if the locationUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/location-users")
    public ResponseEntity<LocationUserDTO> createLocationUser(@RequestBody LocationUserDTO locationUserDTO) throws URISyntaxException {
        log.debug("REST request to save LocationUser : {}", locationUserDTO);
        if (locationUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new locationUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationUserDTO result = locationUserService.save(locationUserDTO);
        return ResponseEntity
            .created(new URI("/api/location-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-users/:id} : Updates an existing locationUser.
     *
     * @param id the id of the locationUserDTO to save.
     * @param locationUserDTO the locationUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationUserDTO,
     * or with status {@code 400 (Bad Request)} if the locationUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/location-users/{id}")
    public ResponseEntity<LocationUserDTO> updateLocationUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationUserDTO locationUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LocationUser : {}, {}", id, locationUserDTO);
        if (locationUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocationUserDTO result = locationUserService.update(locationUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /location-users/:id} : Partial updates given fields of an existing locationUser, field will ignore if it is null
     *
     * @param id the id of the locationUserDTO to save.
     * @param locationUserDTO the locationUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationUserDTO,
     * or with status {@code 400 (Bad Request)} if the locationUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the locationUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/location-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocationUserDTO> partialUpdateLocationUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationUserDTO locationUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocationUser partially : {}, {}", id, locationUserDTO);
        if (locationUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationUserDTO> result = locationUserService.partialUpdate(locationUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /location-users} : get all the locationUsers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationUsers in body.
     */
    @GetMapping("/location-users")
    public List<LocationUserDTO> getAllLocationUsers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all LocationUsers");
        return locationUserService.findAll();
    }

    /**
     * {@code GET  /location-users/:id} : get the "id" locationUser.
     *
     * @param id the id of the locationUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-users/{id}")
    public ResponseEntity<LocationUserDTO> getLocationUser(@PathVariable Long id) {
        log.debug("REST request to get LocationUser : {}", id);
        Optional<LocationUserDTO> locationUserDTO = locationUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationUserDTO);
    }

    /**
     * {@code DELETE  /location-users/:id} : delete the "id" locationUser.
     *
     * @param id the id of the locationUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/location-users/{id}")
    public ResponseEntity<Void> deleteLocationUser(@PathVariable Long id) {
        log.debug("REST request to delete LocationUser : {}", id);
        locationUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
