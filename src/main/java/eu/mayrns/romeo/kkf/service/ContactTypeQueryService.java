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

import eu.mayrns.romeo.kkf.domain.ContactType;
import eu.mayrns.romeo.kkf.domain.*; // for static metamodels
import eu.mayrns.romeo.kkf.repository.ContactTypeRepository;
import eu.mayrns.romeo.kkf.repository.search.ContactTypeSearchRepository;
import eu.mayrns.romeo.kkf.service.dto.ContactTypeCriteria;


/**
 * Service for executing complex queries for ContactType entities in the database.
 * The main input is a {@link ContactTypeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactType} or a {@link Page} of {@link ContactType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactTypeQueryService extends QueryService<ContactType> {

    private final Logger log = LoggerFactory.getLogger(ContactTypeQueryService.class);


    private final ContactTypeRepository contactTypeRepository;

    private final ContactTypeSearchRepository contactTypeSearchRepository;

    public ContactTypeQueryService(ContactTypeRepository contactTypeRepository, ContactTypeSearchRepository contactTypeSearchRepository) {
        this.contactTypeRepository = contactTypeRepository;
        this.contactTypeSearchRepository = contactTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContactType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactType> findByCriteria(ContactTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ContactType> specification = createSpecification(criteria);
        return contactTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ContactType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactType> findByCriteria(ContactTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ContactType> specification = createSpecification(criteria);
        return contactTypeRepository.findAll(specification, page);
    }

    /**
     * Function to convert ContactTypeCriteria to a {@link Specifications}
     */
    private Specifications<ContactType> createSpecification(ContactTypeCriteria criteria) {
        Specifications<ContactType> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ContactType_.id));
            }
            if (criteria.getOmschrijving() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOmschrijving(), ContactType_.omschrijving));
            }
        }
        return specification;
    }

}
