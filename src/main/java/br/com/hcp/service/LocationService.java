package br.com.hcp.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.hcp.domain.Location;
import br.com.hcp.domain.LocationUser;
import br.com.hcp.domain.enumeration.LocationType;
import br.com.hcp.repository.LocationRepository;
import br.com.hcp.repository.LocationUserRepository;
import br.com.hcp.service.dto.LocationDTO;
import br.com.hcp.service.mapper.LocationMapper;

/**
 * Service Implementation for managing {@link Location}.
 */
@Service
@Transactional
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;
    
    private final LocationUserRepository locationUserRepository;

    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, LocationUserRepository locationUserRepository,
			LocationMapper locationMapper) {
		super();
		this.locationRepository = locationRepository;
		this.locationUserRepository = locationUserRepository;
		this.locationMapper = locationMapper;
	}

	/**
     * Save a location.
     *
     * @param locationDTO the entity to save.
     * @return the persisted entity.
     */
    public LocationDTO save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);
        
        Location location = locationMapper.toEntity(locationDTO);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (locationDTO.getExistedId() != null) {
        	location.setId(locationDTO.getExistedId());
        	locationUserRepository.save(new LocationUser(auth.getName(), locationDTO.getType(), location));
        	return locationMapper.toDto(location);
        }
        
        location = locationRepository.save(location);
        locationUserRepository.save(new LocationUser(auth.getName(), locationDTO.getType(), location));
        
        return locationMapper.toDto(location);
    }

    /**
     * Update a location.
     *
     * @param locationDTO the entity to save.
     * @return the persisted entity.
     */
    public LocationDTO update(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);
        Location location = locationMapper.toEntity(locationDTO);
        location = locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    /**
     * Partially update a location.
     *
     * @param locationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocationDTO> partialUpdate(LocationDTO locationDTO) {
        log.debug("Request to partially update Location : {}", locationDTO);

        return locationRepository
            .findById(locationDTO.getId())
            .map(existingLocation -> {
                locationMapper.partialUpdate(existingLocation, locationDTO);

                return existingLocation;
            })
            .map(locationRepository::save)
            .map(locationMapper::toDto);
    }

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LocationDTO> findAllByLogin() {
        log.debug("Request to get all Locations");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return locationRepository.findLocationsByLogin(auth.getName());
    }

    /**
     * Get one location by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocationDTO> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id).map(locationMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public LocationDTO findByLoginAndLocationType(String login, LocationType locationType) {
        log.debug("Request to get Location : login {}, locationType {}", login, locationType);
        List<LocationDTO> result = locationRepository.findByLoginAndLocationType(login, locationType);
        
        if (result.isEmpty()) {
        	return null;
        }
        
        return result.get(0);
    }
    
    @Transactional(readOnly = true)
    public Optional<LocationDTO> findByZipcodeAndNumber(String zipcode, String number) {
        log.debug("Request to get Location : zipcode {}, number {}", zipcode, number);
        
        Optional<Location> location = locationRepository.findByZipcodeAndNumber(zipcode, number);
        
        if (location.isEmpty()) {
        	Optional.empty();
        }
        
        return location.map(locationMapper::toDto);
    }

    /**
     * Delete the location by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }
}
