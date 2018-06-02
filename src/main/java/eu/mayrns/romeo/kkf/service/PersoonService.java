package eu.mayrns.romeo.kkf.service;

import eu.mayrns.romeo.kkf.domain.Persoon;
import eu.mayrns.romeo.kkf.repository.PersoonRepository;
import eu.mayrns.romeo.kkf.repository.search.PersoonSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Persoon.
 */
@Service
@Transactional
public class PersoonService {

    private final Logger log = LoggerFactory.getLogger(PersoonService.class);

    private final PersoonRepository persoonRepository;

    private final PersoonSearchRepository persoonSearchRepository;

    public PersoonService(PersoonRepository persoonRepository, PersoonSearchRepository persoonSearchRepository) {
        this.persoonRepository = persoonRepository;
        this.persoonSearchRepository = persoonSearchRepository;
    }

    /**
     * Save a persoon.
     *
     * @param persoon the entity to save
     * @return the persisted entity
     */
    public Persoon save(Persoon persoon) {
        log.debug("Request to save Persoon : {}", persoon);
        Persoon result = persoonRepository.save(persoon);
        persoonSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the persoons.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Persoon> findAll(Pageable pageable) {
        log.debug("Request to get all Persoons");
        return persoonRepository.findAll(pageable);
    }

    /**
     * Get one persoon by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Persoon findOne(Long id) {
        log.debug("Request to get Persoon : {}", id);
        return persoonRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the persoon by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Persoon : {}", id);
        persoonRepository.delete(id);
        persoonSearchRepository.delete(id);
    }

    /**
     * Search for the persoon corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Persoon> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Persoons for query {}", query);
        Page<Persoon> result = persoonSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
