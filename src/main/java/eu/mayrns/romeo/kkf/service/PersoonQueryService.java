package eu.mayrns.romeo.kkf.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import eu.mayrns.romeo.kkf.domain.Persoon;
import eu.mayrns.romeo.kkf.domain.*; // for static metamodels
import eu.mayrns.romeo.kkf.repository.PersoonRepository;
import eu.mayrns.romeo.kkf.repository.search.PersoonSearchRepository;
import eu.mayrns.romeo.kkf.service.dto.PersoonCriteria;


/**
 * Service for executing complex queries for Persoon entities in the database.
 * The main input is a {@link PersoonCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Persoon} or a {@link Page} of {@link Persoon} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersoonQueryService extends QueryService<Persoon> {

    private final Logger log = LoggerFactory.getLogger(PersoonQueryService.class);


    private final PersoonRepository persoonRepository;

    private final PersoonSearchRepository persoonSearchRepository;

    public PersoonQueryService(PersoonRepository persoonRepository, PersoonSearchRepository persoonSearchRepository) {
        this.persoonRepository = persoonRepository;
        this.persoonSearchRepository = persoonSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Persoon} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Persoon> findByCriteria(PersoonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Persoon> specification = createSpecification(criteria);
        return persoonRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Persoon} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Persoon> findByCriteria(PersoonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Persoon> specification = createSpecification(criteria);
        return persoonRepository.findAll(specification, page);
    }

    /**
     * Function to convert PersoonCriteria to a {@link Specifications}
     */
    private Specifications<Persoon> createSpecification(PersoonCriteria criteria) {
        Specifications<Persoon> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Persoon_.id));
            }
            if (criteria.getVoornaam() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVoornaam(), Persoon_.voornaam));
            }
            if (criteria.getFamilienaam() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamilienaam(), Persoon_.familienaam));
            }
            if (criteria.getGeboortedatum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGeboortedatum(), Persoon_.geboortedatum));
            }
            if (criteria.getContactInfoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getContactInfoId(), Persoon_.contactInfo, Contact_.id));
            }
            if (criteria.getGeslachtId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getGeslachtId(), Persoon_.geslacht, Geslacht_.id));
            }
            if (criteria.getAdresInfoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAdresInfoId(), Persoon_.adresInfo, Adres_.id));
            }
            if (criteria.getRelatiesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getRelatiesId(), Persoon_.relaties, Relatie_.id));
            }
            if (criteria.getContactId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getContactId(), Persoon_.contact, Contact_.id));
            }
        }
        return specification;
    }

}
