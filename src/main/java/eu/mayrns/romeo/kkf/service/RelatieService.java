package eu.mayrns.romeo.kkf.service;

import eu.mayrns.romeo.kkf.domain.Relatie;
import eu.mayrns.romeo.kkf.repository.RelatieRepository;
import eu.mayrns.romeo.kkf.repository.search.RelatieSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Relatie.
 */
@Service
@Transactional
public class RelatieService {

    private final Logger log = LoggerFactory.getLogger(RelatieService.class);

    private final RelatieRepository relatieRepository;

    private final RelatieSearchRepository relatieSearchRepository;

    public RelatieService(RelatieRepository relatieRepository, RelatieSearchRepository relatieSearchRepository) {
        this.relatieRepository = relatieRepository;
        this.relatieSearchRepository = relatieSearchRepository;
    }

    /**
     * Save a relatie.
     *
     * @param relatie the entity to save
     * @return the persisted entity
     */
    public Relatie save(Relatie relatie) {
        log.debug("Request to save Relatie : {}", relatie);
        Relatie result = relatieRepository.save(relatie);
        relatieSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the relaties.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Relatie> findAll(Pageable pageable) {
        log.debug("Request to get all Relaties");
        return relatieRepository.findAll(pageable);
    }

    /**
     * Get one relatie by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Relatie findOne(Long id) {
        log.debug("Request to get Relatie : {}", id);
        return relatieRepository.findOne(id);
    }

    /**
     * Delete the relatie by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Relatie : {}", id);
        relatieRepository.delete(id);
        relatieSearchRepository.delete(id);
    }

    /**
     * Search for the relatie corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Relatie> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Relaties for query {}", query);
        Page<Relatie> result = relatieSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
