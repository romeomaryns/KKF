package eu.mayrns.romeo.kkf.repository;

import eu.mayrns.romeo.kkf.domain.Relatie;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Relatie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelatieRepository extends JpaRepository<Relatie, Long>, JpaSpecificationExecutor<Relatie> {

}
