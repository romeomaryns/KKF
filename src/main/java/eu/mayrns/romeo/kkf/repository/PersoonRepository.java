package eu.mayrns.romeo.kkf.repository;

import eu.mayrns.romeo.kkf.domain.Persoon;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Persoon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersoonRepository extends JpaRepository<Persoon, Long>, JpaSpecificationExecutor<Persoon> {
    @Query("select distinct persoon from Persoon persoon left join fetch persoon.relaties")
    List<Persoon> findAllWithEagerRelationships();

    @Query("select persoon from Persoon persoon left join fetch persoon.relaties where persoon.id =:id")
    Persoon findOneWithEagerRelationships(@Param("id") Long id);

}
