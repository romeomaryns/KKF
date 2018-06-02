package eu.mayrns.romeo.kkf.service;

import eu.mayrns.romeo.kkf.domain.RelatieType;
import eu.mayrns.romeo.kkf.repository.RelatieTypeRepository;
import eu.mayrns.romeo.kkf.repository.search.RelatieTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing RelatieType.
 */
@Service
@Transactional
public class RelatieTypeService {

    private final Logger log = LoggerFactory.getLogger(RelatieTypeService.class);

    private final RelatieTypeRepository relatieTypeRepository;

    private final RelatieTypeSearchRepository relatieTypeSearchRepository;

    public RelatieTypeService(RelatieTypeRepository relatieTypeRepository, RelatieTypeSearchRepository relatieTypeSearchRepository) {
        this.relatieTypeRepository = relatieTypeRepository;
        this.relatieTypeSearchRepository = relatieTypeSearchRepository;
    }

    /**
     * Save a relatieType.
     *
     * @param relatieType the entity to save
     * @return the persisted entity
     */
    public RelatieType save(RelatieType relatieType) {
        log.debug("Request to save RelatieType : {}", relatieType);
        RelatieType result = relatieTypeRepository.save(relatieType);
        relatieTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the relatieTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RelatieType> findAll(Pageable pageable) {
        log.debug("Request to get all RelatieTypes");
        return relatieTypeRepository.findAll(pageable);
    }

    /**
     * Get one relatieType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RelatieType findOne(Long id) {
        log.debug("Request to get RelatieType : {}", id);
        return relatieTypeRepository.findOne(id);
    }

    /**
     * Delete the relatieType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RelatieType : {}", id);
        relatieTypeRepository.delete(id);
        relatieTypeSearchRepository.delete(id);
    }

    /**
     * Search for the relatieType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RelatieType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RelatieTypes for query {}", query);
        Page<RelatieType> result = relatieTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
