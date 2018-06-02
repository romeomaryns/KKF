package eu.mayrns.romeo.kkf.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import eu.mayrns.romeo.kkf.domain.Contact;
import eu.mayrns.romeo.kkf.domain.*; // for static metamodels
import eu.mayrns.romeo.kkf.repository.ContactRepository;
import eu.mayrns.romeo.kkf.repository.search.ContactSearchRepository;
import eu.mayrns.romeo.kkf.service.dto.ContactCriteria;


/**
 * Service for executing complex queries for Contact entities in the database.
 * The main input is a {@link ContactCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Contact} or a {@link Page} of {@link Contact} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactQueryService extends QueryService<Contact> {

    private final Logger log = LoggerFactory.getLogger(ContactQueryService.class);


    private final ContactRepository contactRepository;

    private final ContactSearchRepository contactSearchRepository;

    public ContactQueryService(ContactRepository contactRepository, ContactSearchRepository contactSearchRepository) {
        this.contactRepository = contactRepository;
        this.contactSearchRepository = contactSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Contact} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Contact> findByCriteria(ContactCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Contact> specification = createSpecification(criteria);
        return contactRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Contact} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Contact> findByCriteria(ContactCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Contact> specification = createSpecification(criteria);
        return contactRepository.findAll(specification, page);
    }

    /**
     * Function to convert ContactCriteria to a {@link Specifications}
     */
    private Specifications<Contact> createSpecification(ContactCriteria criteria) {
        Specifications<Contact> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Contact_.id));
            }
            if (criteria.getOmschrijving() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOmschrijving(), Contact_.omschrijving));
            }
            if (criteria.getPersoonId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPersoonId(), Contact_.persoons, Persoon_.id));
            }
            if (criteria.getContactTypeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getContactTypeId(), Contact_.contactType, ContactType_.id));
            }
        }
        return specification;
    }

}
