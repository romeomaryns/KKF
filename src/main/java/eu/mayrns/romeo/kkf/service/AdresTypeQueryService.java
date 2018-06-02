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

import eu.mayrns.romeo.kkf.domain.AdresType;
import eu.mayrns.romeo.kkf.domain.*; // for static metamodels
import eu.mayrns.romeo.kkf.repository.AdresTypeRepository;
import eu.mayrns.romeo.kkf.repository.search.AdresTypeSearchRepository;
import eu.mayrns.romeo.kkf.service.dto.AdresTypeCriteria;


/**
 * Service for executing complex queries for AdresType entities in the database.
 * The main input is a {@link AdresTypeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AdresType} or a {@link Page} of {@link AdresType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdresTypeQueryService extends QueryService<AdresType> {

    private final Logger log = LoggerFactory.getLogger(AdresTypeQueryService.class);


    private final AdresTypeRepository adresTypeRepository;

    private final AdresTypeSearchRepository adresTypeSearchRepository;

    public AdresTypeQueryService(AdresTypeRepository adresTypeRepository, AdresTypeSearchRepository adresTypeSearchRepository) {
        this.adresTypeRepository = adresTypeRepository;
        this.adresTypeSearchRepository = adresTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AdresType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdresType> findByCriteria(AdresTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<AdresType> specification = createSpecification(criteria);
        return adresTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AdresType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdresType> findByCriteria(AdresTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<AdresType> specification = createSpecification(criteria);
        return adresTypeRepository.findAll(specification, page);
    }

    /**
     * Function to convert AdresTypeCriteria to a {@link Specifications}
     */
    private Specifications<AdresType> createSpecification(AdresTypeCriteria criteria) {
        Specifications<AdresType> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AdresType_.id));
            }
            if (criteria.getOmschrijving() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOmschrijving(), AdresType_.omschrijving));
            }
        }
        return specification;
    }

}
