package br.com.hcp.repository;

import br.com.hcp.domain.LocationUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocationUser entity.
 */
@Repository
public interface LocationUserRepository extends JpaRepository<LocationUser, Long> {
    default Optional<LocationUser> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LocationUser> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LocationUser> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct locationUser from LocationUser locationUser left join fetch locationUser.location",
        countQuery = "select count(distinct locationUser) from LocationUser locationUser"
    )
    Page<LocationUser> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct locationUser from LocationUser locationUser left join fetch locationUser.location")
    List<LocationUser> findAllWithToOneRelationships();

    @Query("select locationUser from LocationUser locationUser left join fetch locationUser.location where locationUser.id =:id")
    Optional<LocationUser> findOneWithToOneRelationships(@Param("id") Long id);
    
    Optional<LocationUser> findByLocationIdAndLogin(Long locationId, String login);
}
