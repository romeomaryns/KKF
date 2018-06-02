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

import eu.mayrns.romeo.kkf.domain.Relatie;
import eu.mayrns.romeo.kkf.domain.*; // for static metamodels
import eu.mayrns.romeo.kkf.repository.RelatieRepository;
import eu.mayrns.romeo.kkf.repository.search.RelatieSearchRepository;
import eu.mayrns.romeo.kkf.service.dto.RelatieCriteria;


/**
 * Service for executing complex queries for Relatie entities in the database.
 * The main input is a {@link RelatieCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Relatie} or a {@link Page} of {@link Relatie} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RelatieQueryService extends QueryService<Relatie> {

    private final Logger log = LoggerFactory.getLogger(RelatieQueryService.class);


    private final RelatieRepository relatieRepository;

    private final RelatieSearchRepository relatieSearchRepository;

    public RelatieQueryService(RelatieRepository relatieRepository, RelatieSearchRepository relatieSearchRepository) {
        this.relatieRepository = relatieRepository;
        this.relatieSearchRepository = relatieSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Relatie} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Relatie> findByCriteria(RelatieCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Relatie> specification = createSpecification(criteria);
        return relatieRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Relatie} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Relatie> findByCriteria(RelatieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Relatie> specification = createSpecification(criteria);
        return relatieRepository.findAll(specification, page);
    }

    /**
     * Function to convert RelatieCriteria to a {@link Specifications}
     */
    private Specifications<Relatie> createSpecification(RelatieCriteria criteria) {
        Specifications<Relatie> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Relatie_.id));
            }
            if (criteria.getOmschrijving() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOmschrijving(), Relatie_.omschrijving));
            }
            if (criteria.getRelatieTypeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getRelatieTypeId(), Relatie_.relatieType, RelatieType_.id));
            }
            if (criteria.getPersonenId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPersonenId(), Relatie_.personens, Persoon_.id));
            }
        }
        return specification;
    }

}
