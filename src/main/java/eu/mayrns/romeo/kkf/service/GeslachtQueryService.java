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

import eu.mayrns.romeo.kkf.domain.Geslacht;
import eu.mayrns.romeo.kkf.domain.*; // for static metamodels
import eu.mayrns.romeo.kkf.repository.GeslachtRepository;
import eu.mayrns.romeo.kkf.repository.search.GeslachtSearchRepository;
import eu.mayrns.romeo.kkf.service.dto.GeslachtCriteria;


/**
 * Service for executing complex queries for Geslacht entities in the database.
 * The main input is a {@link GeslachtCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Geslacht} or a {@link Page} of {@link Geslacht} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeslachtQueryService extends QueryService<Geslacht> {

    private final Logger log = LoggerFactory.getLogger(GeslachtQueryService.class);


    private final GeslachtRepository geslachtRepository;

    private final GeslachtSearchRepository geslachtSearchRepository;

    public GeslachtQueryService(GeslachtRepository geslachtRepository, GeslachtSearchRepository geslachtSearchRepository) {
        this.geslachtRepository = geslachtRepository;
        this.geslachtSearchRepository = geslachtSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Geslacht} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Geslacht> findByCriteria(GeslachtCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Geslacht> specification = createSpecification(criteria);
        return geslachtRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Geslacht} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Geslacht> findByCriteria(GeslachtCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Geslacht> specification = createSpecification(criteria);
        return geslachtRepository.findAll(specification, page);
    }

    /**
     * Function to convert GeslachtCriteria to a {@link Specifications}
     */
    private Specifications<Geslacht> createSpecification(GeslachtCriteria criteria) {
        Specifications<Geslacht> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Geslacht_.id));
            }
            if (criteria.getOmschrijving() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOmschrijving(), Geslacht_.omschrijving));
            }
        }
        return specification;
    }

}
