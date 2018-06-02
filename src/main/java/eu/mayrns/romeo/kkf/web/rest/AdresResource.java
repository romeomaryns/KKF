package eu.mayrns.romeo.kkf.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.mayrns.romeo.kkf.domain.Adres;
import eu.mayrns.romeo.kkf.service.AdresService;
import eu.mayrns.romeo.kkf.web.rest.errors.BadRequestAlertException;
import eu.mayrns.romeo.kkf.web.rest.util.HeaderUtil;
import eu.mayrns.romeo.kkf.web.rest.util.PaginationUtil;
import eu.mayrns.romeo.kkf.service.dto.AdresCriteria;
import eu.mayrns.romeo.kkf.service.AdresQueryService;
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
 * REST controller for managing Adres.
 */
@RestController
@RequestMapping("/api")
public class AdresResource {

    private final Logger log = LoggerFactory.getLogger(AdresResource.class);

    private static final String ENTITY_NAME = "adres";

    private final AdresService adresService;

    private final AdresQueryService adresQueryService;

    public AdresResource(AdresService adresService, AdresQueryService adresQueryService) {
        this.adresService = adresService;
        this.adresQueryService = adresQueryService;
    }

    /**
     * POST  /adres : Create a new adres.
     *
     * @param adres the adres to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adres, or with status 400 (Bad Request) if the adres has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/adres")
    @Timed
    public ResponseEntity<Adres> createAdres(@Valid @RequestBody Adres adres) throws URISyntaxException {
        log.debug("REST request to save Adres : {}", adres);
        if (adres.getId() != null) {
            throw new BadRequestAlertException("A new adres cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Adres result = adresService.save(adres);
        return ResponseEntity.created(new URI("/api/adres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /adres : Updates an existing adres.
     *
     * @param adres the adres to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adres,
     * or with status 400 (Bad Request) if the adres is not valid,
     * or with status 500 (Internal Server Error) if the adres couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/adres")
    @Timed
    public ResponseEntity<Adres> updateAdres(@Valid @RequestBody Adres adres) throws URISyntaxException {
        log.debug("REST request to update Adres : {}", adres);
        if (adres.getId() == null) {
            return createAdres(adres);
        }
        Adres result = adresService.save(adres);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, adres.getId().toString()))
            .body(result);
    }

    /**
     * GET  /adres : get all the adres.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of adres in body
     */
    @GetMapping("/adres")
    @Timed
    public ResponseEntity<List<Adres>> getAllAdres(AdresCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Adres by criteria: {}", criteria);
        Page<Adres> page = adresQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/adres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /adres/:id : get the "id" adres.
     *
     * @param id the id of the adres to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adres, or with status 404 (Not Found)
     */
    @GetMapping("/adres/{id}")
    @Timed
    public ResponseEntity<Adres> getAdres(@PathVariable Long id) {
        log.debug("REST request to get Adres : {}", id);
        Adres adres = adresService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(adres));
    }

    /**
     * DELETE  /adres/:id : delete the "id" adres.
     *
     * @param id the id of the adres to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/adres/{id}")
    @Timed
    public ResponseEntity<Void> deleteAdres(@PathVariable Long id) {
        log.debug("REST request to delete Adres : {}", id);
        adresService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/adres?query=:query : search for the adres corresponding
     * to the query.
     *
     * @param query the query of the adres search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/adres")
    @Timed
    public ResponseEntity<List<Adres>> searchAdres(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Adres for query {}", query);
        Page<Adres> page = adresService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/adres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
