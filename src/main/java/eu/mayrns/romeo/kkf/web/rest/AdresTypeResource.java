package eu.mayrns.romeo.kkf.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.mayrns.romeo.kkf.domain.AdresType;
import eu.mayrns.romeo.kkf.service.AdresTypeService;
import eu.mayrns.romeo.kkf.web.rest.errors.BadRequestAlertException;
import eu.mayrns.romeo.kkf.web.rest.util.HeaderUtil;
import eu.mayrns.romeo.kkf.web.rest.util.PaginationUtil;
import eu.mayrns.romeo.kkf.service.dto.AdresTypeCriteria;
import eu.mayrns.romeo.kkf.service.AdresTypeQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AdresType.
 */
@RestController
@RequestMapping("/api")
public class AdresTypeResource {

    private final Logger log = LoggerFactory.getLogger(AdresTypeResource.class);

    private static final String ENTITY_NAME = "adresType";

    private final AdresTypeService adresTypeService;

    private final AdresTypeQueryService adresTypeQueryService;

    public AdresTypeResource(AdresTypeService adresTypeService, AdresTypeQueryService adresTypeQueryService) {
        this.adresTypeService = adresTypeService;
        this.adresTypeQueryService = adresTypeQueryService;
    }

    /**
     * POST  /adres-types : Create a new adresType.
     *
     * @param adresType the adresType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adresType, or with status 400 (Bad Request) if the adresType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/adres-types")
    @Timed
    public ResponseEntity<AdresType> createAdresType(@Valid @RequestBody AdresType adresType) throws URISyntaxException {
        log.debug("REST request to save AdresType : {}", adresType);
        if (adresType.getId() != null) {
            throw new BadRequestAlertException("A new adresType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdresType result = adresTypeService.save(adresType);
        return ResponseEntity.created(new URI("/api/adres-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /adres-types : Updates an existing adresType.
     *
     * @param adresType the adresType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adresType,
     * or with status 400 (Bad Request) if the adresType is not valid,
     * or with status 500 (Internal Server Error) if the adresType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/adres-types")
    @Timed
    public ResponseEntity<AdresType> updateAdresType(@Valid @RequestBody AdresType adresType) throws URISyntaxException {
        log.debug("REST request to update AdresType : {}", adresType);
        if (adresType.getId() == null) {
            return createAdresType(adresType);
        }
        AdresType result = adresTypeService.save(adresType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, adresType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /adres-types : get all the adresTypes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of adresTypes in body
     */
    @GetMapping("/adres-types")
    @Timed
    public ResponseEntity<List<AdresType>> getAllAdresTypes(AdresTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AdresTypes by criteria: {}", criteria);
        Page<AdresType> page = adresTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/adres-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /adres-types/:id : get the "id" adresType.
     *
     * @param id the id of the adresType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adresType, or with status 404 (Not Found)
     */
    @GetMapping("/adres-types/{id}")
    @Timed
    public ResponseEntity<AdresType> getAdresType(@PathVariable Long id) {
        log.debug("REST request to get AdresType : {}", id);
        AdresType adresType = adresTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(adresType));
    }

    /**
     * DELETE  /adres-types/:id : delete the "id" adresType.
     *
     * @param id the id of the adresType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/adres-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteAdresType(@PathVariable Long id) {
        log.debug("REST request to delete AdresType : {}", id);
        adresTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/adres-types?query=:query : search for the adresType corresponding
     * to the query.
     *
     * @param query the query of the adresType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/adres-types")
    @Timed
    public ResponseEntity<List<AdresType>> searchAdresTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AdresTypes for query {}", query);
        Page<AdresType> page = adresTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/adres-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
