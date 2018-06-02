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

import eu.mayrns.romeo.kkf.domain.Adres;
import eu.mayrns.romeo.kkf.domain.*; // for static metamodels
import eu.mayrns.romeo.kkf.repository.AdresRepository;
import eu.mayrns.romeo.kkf.repository.search.AdresSearchRepository;
import eu.mayrns.romeo.kkf.service.dto.AdresCriteria;


/**
 * Service for executing complex queries for Adres entities in the database.
 * The main input is a {@link AdresCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Adres} or a {@link Page} of {@link Adres} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdresQueryService extends QueryService<Adres> {

    private final Logger log = LoggerFactory.getLogger(AdresQueryService.class);


    private final AdresRepository adresRepository;

    private final AdresSearchRepository adresSearchRepository;

    public AdresQueryService(AdresRepository adresRepository, AdresSearchRepository adresSearchRepository) {
        this.adresRepository = adresRepository;
        this.adresSearchRepository = adresSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Adres} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Adres> findByCriteria(AdresCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Adres> specification = createSpecification(criteria);
        return adresRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Adres} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Adres> findByCriteria(AdresCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Adres> specification = createSpecification(criteria);
        return adresRepository.findAll(specification, page);
    }

    /**
     * Function to convert AdresCriteria to a {@link Specifications}
     */
    private Specifications<Adres> createSpecification(AdresCriteria criteria) {
        Specifications<Adres> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Adres_.id));
            }
            if (criteria.getStraatnaam() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStraatnaam(), Adres_.straatnaam));
            }
            if (criteria.getHuisnummer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHuisnummer(), Adres_.huisnummer));
            }
            if (criteria.getPostcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostcode(), Adres_.postcode));
            }
            if (criteria.getStad() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStad(), Adres_.stad));
            }
            if (criteria.getAdresTypeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAdresTypeId(), Adres_.adresType, AdresType_.id));
            }
        }
        return specification;
    }

}
