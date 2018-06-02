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

import eu.mayrns.romeo.kkf.domain.RelatieType;
import eu.mayrns.romeo.kkf.domain.*; // for static metamodels
import eu.mayrns.romeo.kkf.repository.RelatieTypeRepository;
import eu.mayrns.romeo.kkf.repository.search.RelatieTypeSearchRepository;
import eu.mayrns.romeo.kkf.service.dto.RelatieTypeCriteria;


/**
 * Service for executing complex queries for RelatieType entities in the database.
 * The main input is a {@link RelatieTypeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RelatieType} or a {@link Page} of {@link RelatieType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RelatieTypeQueryService extends QueryService<RelatieType> {

    private final Logger log = LoggerFactory.getLogger(RelatieTypeQueryService.class);


    private final RelatieTypeRepository relatieTypeRepository;

    private final RelatieTypeSearchRepository relatieTypeSearchRepository;

    public RelatieTypeQueryService(RelatieTypeRepository relatieTypeRepository, RelatieTypeSearchRepository relatieTypeSearchRepository) {
        this.relatieTypeRepository = relatieTypeRepository;
        this.relatieTypeSearchRepository = relatieTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RelatieType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RelatieType> findByCriteria(RelatieTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<RelatieType> specification = createSpecification(criteria);
        return relatieTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RelatieType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RelatieType> findByCriteria(RelatieTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<RelatieType> specification = createSpecification(criteria);
        return relatieTypeRepository.findAll(specification, page);
    }

    /**
     * Function to convert RelatieTypeCriteria to a {@link Specifications}
     */
    private Specifications<RelatieType> createSpecification(RelatieTypeCriteria criteria) {
        Specifications<RelatieType> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RelatieType_.id));
            }
            if (criteria.getOmschrijving() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOmschrijving(), RelatieType_.omschrijving));
            }
        }
        return specification;
    }

}
