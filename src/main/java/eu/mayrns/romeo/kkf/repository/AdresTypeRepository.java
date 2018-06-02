package eu.mayrns.romeo.kkf.repository;

import eu.mayrns.romeo.kkf.domain.AdresType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AdresType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdresTypeRepository extends JpaRepository<AdresType, Long>, JpaSpecificationExecutor<AdresType> {

}
