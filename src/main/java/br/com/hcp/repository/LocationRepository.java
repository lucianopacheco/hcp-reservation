package br.com.hcp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.hcp.domain.Location;
import br.com.hcp.service.dto.LocationDTO;

/**
 * Spring Data SQL repository for the Location entity.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
	
	@Query("SELECT new br.com.hcp.service.dto.LocationDTO(l.id, l.name, l.zipcode, l.address, l.number, l.city, l.state, lu.locationType) "
			+ "FROM Location l JOIN LocationUser lu ON lu.location.id = l.id "
			+ "WHERE lu.login = :login")
	List<LocationDTO> findLocationsByLogin(@Param("login") String login);
	
	@Query
	Optional<Location> findByZipcodeAndNumber(String zipcode, String number);
	
}
