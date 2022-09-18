package br.com.hcp.repository;

import br.com.hcp.domain.Condominium;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Condominium entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CondominiumRepository extends JpaRepository<Condominium, Long> {}
