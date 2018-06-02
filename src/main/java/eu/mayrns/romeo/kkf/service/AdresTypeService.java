package eu.mayrns.romeo.kkf.service;

import eu.mayrns.romeo.kkf.domain.AdresType;
import eu.mayrns.romeo.kkf.repository.AdresTypeRepository;
import eu.mayrns.romeo.kkf.repository.search.AdresTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AdresType.
 */
@Service
@Transactional
public class AdresTypeService {

    private final Logger log = LoggerFactory.getLogger(AdresTypeService.class);

    private final AdresTypeRepository adresTypeRepository;

    private final AdresTypeSearchRepository adresTypeSearchRepository;

    public AdresTypeService(AdresTypeRepository adresTypeRepository, AdresTypeSearchRepository adresTypeSearchRepository) {
        this.adresTypeRepository = adresTypeRepository;
        this.adresTypeSearchRepository = adresTypeSearchRepository;
    }

    /**
     * Save a adresType.
     *
     * @param adresType the entity to save
     * @return the persisted entity
     */
    public AdresType save(AdresType adresType) {
        log.debug("Request to save AdresType : {}", adresType);
        AdresType result = adresTypeRepository.save(adresType);
        adresTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the adresTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AdresType> findAll(Pageable pageable) {
        log.debug("Request to get all AdresTypes");
        return adresTypeRepository.findAll(pageable);
    }

    /**
     * Get one adresType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public AdresType findOne(Long id) {
        log.debug("Request to get AdresType : {}", id);
        return adresTypeRepository.findOne(id);
    }

    /**
     * Delete the adresType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AdresType : {}", id);
        adresTypeRepository.delete(id);
        adresTypeSearchRepository.delete(id);
    }

    /**
     * Search for the adresType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AdresType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AdresTypes for query {}", query);
        Page<AdresType> result = adresTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
