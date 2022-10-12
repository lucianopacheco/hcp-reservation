package br.com.hcp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.hcp.domain.Reservation;

/**
 * Spring Data SQL repository for the Reservation entity.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
	
	@Query("select r from Reservation r where r.passengerLogin = :login")
	public Page<Reservation> findByLoginToUserPassenger(Pageable pageable, @Param("login") String login);
	
	@Query("select r from Reservation r where r.trip.driverLogin = :login")
	public Page<Reservation> findByLoginToUserDriver(Pageable pageable, @Param("login") String login);
	
}
