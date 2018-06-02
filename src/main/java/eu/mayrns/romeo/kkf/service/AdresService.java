package eu.mayrns.romeo.kkf.service;

import eu.mayrns.romeo.kkf.domain.Adres;
import eu.mayrns.romeo.kkf.repository.AdresRepository;
import eu.mayrns.romeo.kkf.repository.search.AdresSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Adres.
 */
@Service
@Transactional
public class AdresService {

    private final Logger log = LoggerFactory.getLogger(AdresService.class);

    private final AdresRepository adresRepository;

    private final AdresSearchRepository adresSearchRepository;

    public AdresService(AdresRepository adresRepository, AdresSearchRepository adresSearchRepository) {
        this.adresRepository = adresRepository;
        this.adresSearchRepository = adresSearchRepository;
    }

    /**
     * Save a adres.
     *
     * @param adres the entity to save
     * @return the persisted entity
     */
    public Adres save(Adres adres) {
        log.debug("Request to save Adres : {}", adres);
        Adres result = adresRepository.save(adres);
        adresSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the adres.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Adres> findAll(Pageable pageable) {
        log.debug("Request to get all Adres");
        return adresRepository.findAll(pageable);
    }

    /**
     * Get one adres by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Adres findOne(Long id) {
        log.debug("Request to get Adres : {}", id);
        return adresRepository.findOne(id);
    }

    /**
     * Delete the adres by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Adres : {}", id);
        adresRepository.delete(id);
        adresSearchRepository.delete(id);
    }

    /**
     * Search for the adres corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Adres> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Adres for query {}", query);
        Page<Adres> result = adresSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
