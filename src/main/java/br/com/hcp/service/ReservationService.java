package br.com.hcp.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.hcp.domain.Reservation;
import br.com.hcp.domain.Trip;
import br.com.hcp.domain.enumeration.ReservationStatus;
import br.com.hcp.repository.ReservationRepository;
import br.com.hcp.repository.TripRepository;
import br.com.hcp.service.dto.ReservationDTO;
import br.com.hcp.service.mapper.ReservationMapper;
import br.com.hcp.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link Reservation}.
 */
@Service
@Transactional
public class ReservationService {

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;

    private final TripRepository tripRepository;
    
    private final ReservationMapper reservationMapper;
    
    public ReservationService(ReservationRepository reservationRepository, TripRepository tripRepository,
			ReservationMapper reservationMapper) {
		this.reservationRepository = reservationRepository;
		this.tripRepository = tripRepository;
		this.reservationMapper = reservationMapper;
	}

	/**
     * Save a reservation.
     *
     * @param reservationDTO the entity to save.
     * @return the persisted entity.
     */
    public ReservationDTO save(ReservationDTO reservationDTO) {
        log.debug("Request to save Reservation : {}", reservationDTO);
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //reservation.setPassengerLogin(auth.getName());
        
        Trip trip = tripRepository.findById(reservation.getTrip().getId())
        		.orElseThrow(EntityNotFoundException::new);
        
        if (trip.getAvailableSeats() == 0) {
        	throw new BadRequestAlertException("Assento não disponível para esta viagem.", "Reservation", "availableSeats");
        }
        
        trip.setAvailableSeats(trip.getAvailableSeats() - 1);
        
        reservation.setTrip(trip);
        
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(reservation);
    }

    /**
     * Update a reservation.
     *
     * @param reservationDTO the entity to save.
     * @return the persisted entity.
     */
    public ReservationDTO update(ReservationDTO reservationDTO) {
        log.debug("Request to save Reservation : {}", reservationDTO);
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(reservation);
    }

    /**
     * Partially update a reservation.
     *
     * @param reservationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReservationDTO> partialUpdate(ReservationDTO reservationDTO) {
        log.debug("Request to partially update Reservation : {}", reservationDTO);

        return reservationRepository
            .findById(reservationDTO.getId())
            .map(existingReservation -> {
                reservationMapper.partialUpdate(existingReservation, reservationDTO);

                return existingReservation;
            })
            .map(reservationRepository::save)
            .map(reservationMapper::toDto);
    }

    /**
     * Get all the reservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReservationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reservations");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return reservationRepository.findByLogin(pageable, auth.getName()).map(reservationMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public Page<ReservationDTO> findAllByLocation(Pageable pageable, Long homeLocationId, Long workLocationId) {
        log.debug("Request to get all Reservations");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return reservationRepository.findByLogin(pageable, auth.getName()).map(reservationMapper::toDto);
    }

    /**
     * Get one reservation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReservationDTO> findOne(Long id) {
        log.debug("Request to get Reservation : {}", id);
        return reservationRepository.findById(id).map(reservationMapper::toDto);
    }

    /**
     * Delete the reservation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to cancel Reservation : {}", id);
        
        Reservation reservation = reservationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        reservation.setStatus(ReservationStatus.CANCELED);
        reservation.getTrip().setAvailableSeats(reservation.getTrip().getAvailableSeats() + 1);
        reservationRepository.save(reservation);
    }
}
