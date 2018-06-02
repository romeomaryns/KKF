package eu.mayrns.romeo.kkf.repository;

import eu.mayrns.romeo.kkf.domain.Geslacht;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Geslacht entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeslachtRepository extends JpaRepository<Geslacht, Long>, JpaSpecificationExecutor<Geslacht> {

}
