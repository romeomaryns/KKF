package eu.mayrns.romeo.kkf.service;

import eu.mayrns.romeo.kkf.domain.Geslacht;
import eu.mayrns.romeo.kkf.repository.GeslachtRepository;
import eu.mayrns.romeo.kkf.repository.search.GeslachtSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Geslacht.
 */
@Service
@Transactional
public class GeslachtService {

    private final Logger log = LoggerFactory.getLogger(GeslachtService.class);

    private final GeslachtRepository geslachtRepository;

    private final GeslachtSearchRepository geslachtSearchRepository;

    public GeslachtService(GeslachtRepository geslachtRepository, GeslachtSearchRepository geslachtSearchRepository) {
        this.geslachtRepository = geslachtRepository;
        this.geslachtSearchRepository = geslachtSearchRepository;
    }

    /**
     * Save a geslacht.
     *
     * @param geslacht the entity to save
     * @return the persisted entity
     */
    public Geslacht save(Geslacht geslacht) {
        log.debug("Request to save Geslacht : {}", geslacht);
        Geslacht result = geslachtRepository.save(geslacht);
        geslachtSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the geslachts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Geslacht> findAll(Pageable pageable) {
        log.debug("Request to get all Geslachts");
        return geslachtRepository.findAll(pageable);
    }

    /**
     * Get one geslacht by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Geslacht findOne(Long id) {
        log.debug("Request to get Geslacht : {}", id);
        return geslachtRepository.findOne(id);
    }

    /**
     * Delete the geslacht by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Geslacht : {}", id);
        geslachtRepository.delete(id);
        geslachtSearchRepository.delete(id);
    }

    /**
     * Search for the geslacht corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Geslacht> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Geslachts for query {}", query);
        Page<Geslacht> result = geslachtSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
