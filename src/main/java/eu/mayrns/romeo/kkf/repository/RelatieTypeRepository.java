package eu.mayrns.romeo.kkf.repository;

import eu.mayrns.romeo.kkf.domain.RelatieType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RelatieType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelatieTypeRepository extends JpaRepository<RelatieType, Long>, JpaSpecificationExecutor<RelatieType> {

}
