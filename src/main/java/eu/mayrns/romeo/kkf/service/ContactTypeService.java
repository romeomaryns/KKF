package eu.mayrns.romeo.kkf.service;

import eu.mayrns.romeo.kkf.domain.ContactType;
import eu.mayrns.romeo.kkf.repository.ContactTypeRepository;
import eu.mayrns.romeo.kkf.repository.search.ContactTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ContactType.
 */
@Service
@Transactional
public class ContactTypeService {

    private final Logger log = LoggerFactory.getLogger(ContactTypeService.class);

    private final ContactTypeRepository contactTypeRepository;

    private final ContactTypeSearchRepository contactTypeSearchRepository;

    public ContactTypeService(ContactTypeRepository contactTypeRepository, ContactTypeSearchRepository contactTypeSearchRepository) {
        this.contactTypeRepository = contactTypeRepository;
        this.contactTypeSearchRepository = contactTypeSearchRepository;
    }

    /**
     * Save a contactType.
     *
     * @param contactType the entity to save
     * @return the persisted entity
     */
    public ContactType save(ContactType contactType) {
        log.debug("Request to save ContactType : {}", contactType);
        ContactType result = contactTypeRepository.save(contactType);
        contactTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the contactTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ContactType> findAll(Pageable pageable) {
        log.debug("Request to get all ContactTypes");
        return contactTypeRepository.findAll(pageable);
    }

    /**
     * Get one contactType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ContactType findOne(Long id) {
        log.debug("Request to get ContactType : {}", id);
        return contactTypeRepository.findOne(id);
    }

    /**
     * Delete the contactType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactType : {}", id);
        contactTypeRepository.delete(id);
        contactTypeSearchRepository.delete(id);
    }

    /**
     * Search for the contactType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ContactType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContactTypes for query {}", query);
        Page<ContactType> result = contactTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
