package br.com.hcp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.hcp.domain.Location;

/**
 * Spring Data SQL repository for the Location entity.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
	
	@Query("SELECT l FROM Location l JOIN LocationUser lu ON lu.location.id = l.id WHERE lu.login = :login")
	List<Location> findLocationsByLogin(@Param("login") String login);
	
	@Query
	Optional<Location> findByZipcodeAndNumber(String zipcode, String number);
	
}
