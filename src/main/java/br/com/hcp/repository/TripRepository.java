package br.com.hcp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.hcp.domain.Trip;

/**
 * Spring Data SQL repository for the Trip entity.
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    default Optional<Trip> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Trip> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Trip> findAllWithEagerRelationships(Pageable pageable, String login) {
        return this.findAllWithToOneRelationships(pageable, login);
    }

    @Query(
        value = "select distinct trip from Trip trip left join fetch trip.vehicle left join fetch trip.from left join fetch trip.to where trip.driverLogin = :login",
        countQuery = "select count(distinct trip) from Trip trip"
    )
    Page<Trip> findAllWithToOneRelationships(Pageable pageable, @Param("login") String login);
    
    @Query(
            value = "select distinct trip from Trip trip left join fetch trip.vehicle left join fetch trip.from left join fetch trip.to where (trip.from.id = :homeLocationId or trip.to.id = :workLocationId)",
            countQuery = "select count(distinct trip) from Trip trip"
    )
    Page<Trip> findAllByLocation(Pageable pageable, @Param("homeLocationId") Long homeLocationId, @Param("workLocationId") Long workLocationId);

    @Query("select distinct trip from Trip trip left join fetch trip.vehicle left join fetch trip.from left join fetch trip.to")
    List<Trip> findAllWithToOneRelationships();

    @Query("select trip from Trip trip left join fetch trip.vehicle left join fetch trip.from left join fetch trip.to where trip.id =:id")
    Optional<Trip> findOneWithToOneRelationships(@Param("id") Long id);
}
