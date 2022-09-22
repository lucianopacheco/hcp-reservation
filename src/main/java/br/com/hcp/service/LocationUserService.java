package br.com.hcp.service;

import br.com.hcp.domain.LocationUser;
import br.com.hcp.repository.LocationUserRepository;
import br.com.hcp.service.dto.LocationUserDTO;
import br.com.hcp.service.mapper.LocationUserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocationUser}.
 */
@Service
@Transactional
public class LocationUserService {

    private final Logger log = LoggerFactory.getLogger(LocationUserService.class);

    private final LocationUserRepository locationUserRepository;

    private final LocationUserMapper locationUserMapper;

    public LocationUserService(LocationUserRepository locationUserRepository, LocationUserMapper locationUserMapper) {
        this.locationUserRepository = locationUserRepository;
        this.locationUserMapper = locationUserMapper;
    }

    /**
     * Save a locationUser.
     *
     * @param locationUserDTO the entity to save.
     * @return the persisted entity.
     */
    public LocationUserDTO save(LocationUserDTO locationUserDTO) {
        log.debug("Request to save LocationUser : {}", locationUserDTO);
        LocationUser locationUser = locationUserMapper.toEntity(locationUserDTO);
        locationUser = locationUserRepository.save(locationUser);
        return locationUserMapper.toDto(locationUser);
    }

    /**
     * Update a locationUser.
     *
     * @param locationUserDTO the entity to save.
     * @return the persisted entity.
     */
    public LocationUserDTO update(LocationUserDTO locationUserDTO) {
        log.debug("Request to save LocationUser : {}", locationUserDTO);
        LocationUser locationUser = locationUserMapper.toEntity(locationUserDTO);
        locationUser = locationUserRepository.save(locationUser);
        return locationUserMapper.toDto(locationUser);
    }

    /**
     * Partially update a locationUser.
     *
     * @param locationUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocationUserDTO> partialUpdate(LocationUserDTO locationUserDTO) {
        log.debug("Request to partially update LocationUser : {}", locationUserDTO);

        return locationUserRepository
            .findById(locationUserDTO.getId())
            .map(existingLocationUser -> {
                locationUserMapper.partialUpdate(existingLocationUser, locationUserDTO);

                return existingLocationUser;
            })
            .map(locationUserRepository::save)
            .map(locationUserMapper::toDto);
    }

    /**
     * Get all the locationUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LocationUserDTO> findAll() {
        log.debug("Request to get all LocationUsers");
        return locationUserRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(locationUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the locationUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LocationUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return locationUserRepository.findAllWithEagerRelationships(pageable).map(locationUserMapper::toDto);
    }

    /**
     * Get one locationUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocationUserDTO> findOne(Long id) {
        log.debug("Request to get LocationUser : {}", id);
        return locationUserRepository.findOneWithEagerRelationships(id).map(locationUserMapper::toDto);
    }

    /**
     * Delete the locationUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LocationUser : {}", id);
        locationUserRepository.deleteById(id);
    }
}
