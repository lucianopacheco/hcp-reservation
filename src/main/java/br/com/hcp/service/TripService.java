package br.com.hcp.service;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.hcp.domain.LocationUser;
import br.com.hcp.domain.Trip;
import br.com.hcp.repository.LocationUserRepository;
import br.com.hcp.repository.TripRepository;
import br.com.hcp.service.dto.TripDTO;
import br.com.hcp.service.mapper.TripMapper;

/**
 * Service Implementation for managing {@link Trip}.
 */
@Service
@Transactional
public class TripService {

    private final Logger log = LoggerFactory.getLogger(TripService.class);

    private final TripRepository tripRepository;

    private final TripMapper tripMapper;
    
    private final LocationUserRepository locationUserRepository;

    public TripService(TripRepository tripRepository, TripMapper tripMapper,
			LocationUserRepository locationUserRepository) {
		super();
		this.tripRepository = tripRepository;
		this.tripMapper = tripMapper;
		this.locationUserRepository = locationUserRepository;
	}

	/**
     * Save a trip.
     *
     * @param tripDTO the entity to save.
     * @return the persisted entity.
     */
    public TripDTO save(TripDTO tripDTO) {
        log.debug("Request to save Trip : {}", tripDTO);
        Trip trip = tripMapper.toEntity(tripDTO);
        trip.setCreatedAt(Instant.now());
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        trip.setDriverLogin(auth.getName());
        
        Optional<LocationUser> locationTo = locationUserRepository.findByLocationIdAndLogin(trip.getTo().getId(), auth.getName());
        
        if (locationTo.isPresent()) {
        	trip.setDestinationType(locationTo.get().getLocationType());
        }
        
        trip = tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    /**
     * Update a trip.
     *
     * @param tripDTO the entity to save.
     * @return the persisted entity.
     */
    public TripDTO update(TripDTO tripDTO) {
        log.debug("Request to save Trip : {}", tripDTO);
        Trip trip = tripMapper.toEntity(tripDTO);
        trip = tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    /**
     * Partially update a trip.
     *
     * @param tripDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TripDTO> partialUpdate(TripDTO tripDTO) {
        log.debug("Request to partially update Trip : {}", tripDTO);

        return tripRepository
            .findById(tripDTO.getId())
            .map(existingTrip -> {
                tripMapper.partialUpdate(existingTrip, tripDTO);

                return existingTrip;
            })
            .map(tripRepository::save)
            .map(tripMapper::toDto);
    }

    /**
     * Get all the trips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TripDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Trips");
        return tripRepository.findAll(pageable).map(tripMapper::toDto);
    }
    
    /**
     * Get all the trips with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TripDTO> findAllWithEagerRelationships(Pageable pageable) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return tripRepository.findAllWithEagerRelationships(pageable, auth.getName()).map(tripMapper::toDto);
    }

    /**
     * Get all the trips by home location and work location
     *
     * @return the list of entities.
     */
    public Page<TripDTO> findAllByLocation(Long homeLocationId, Long workLocationId, Pageable pageable) {
        return tripRepository.findAllByLocation(pageable, homeLocationId, workLocationId).map(tripMapper::toDto);
    }

    /**
     * Get one trip by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TripDTO> findOne(Long id) {
        log.debug("Request to get Trip : {}", id);
        return tripRepository.findOneWithEagerRelationships(id).map(tripMapper::toDto);
    }

    /**
     * Delete the trip by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Trip : {}", id);
        tripRepository.deleteById(id);
    }
}
