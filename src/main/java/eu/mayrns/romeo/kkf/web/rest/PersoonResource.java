package eu.mayrns.romeo.kkf.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.mayrns.romeo.kkf.domain.Persoon;
import eu.mayrns.romeo.kkf.service.PersoonService;
import eu.mayrns.romeo.kkf.web.rest.errors.BadRequestAlertException;
import eu.mayrns.romeo.kkf.web.rest.util.HeaderUtil;
import eu.mayrns.romeo.kkf.web.rest.util.PaginationUtil;
import eu.mayrns.romeo.kkf.service.dto.PersoonCriteria;
import eu.mayrns.romeo.kkf.service.PersoonQueryService;
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
 * REST controller for managing Persoon.
 */
@RestController
@RequestMapping("/api")
public class PersoonResource {

    private final Logger log = LoggerFactory.getLogger(PersoonResource.class);

    private static final String ENTITY_NAME = "persoon";

    private final PersoonService persoonService;

    private final PersoonQueryService persoonQueryService;

    public PersoonResource(PersoonService persoonService, PersoonQueryService persoonQueryService) {
        this.persoonService = persoonService;
        this.persoonQueryService = persoonQueryService;
    }

    /**
     * POST  /persoons : Create a new persoon.
     *
     * @param persoon the persoon to create
     * @return the ResponseEntity with status 201 (Created) and with body the new persoon, or with status 400 (Bad Request) if the persoon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/persoons")
    @Timed
    public ResponseEntity<Persoon> createPersoon(@Valid @RequestBody Persoon persoon) throws URISyntaxException {
        log.debug("REST request to save Persoon : {}", persoon);
        if (persoon.getId() != null) {
            throw new BadRequestAlertException("A new persoon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Persoon result = persoonService.save(persoon);
        return ResponseEntity.created(new URI("/api/persoons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /persoons : Updates an existing persoon.
     *
     * @param persoon the persoon to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated persoon,
     * or with status 400 (Bad Request) if the persoon is not valid,
     * or with status 500 (Internal Server Error) if the persoon couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/persoons")
    @Timed
    public ResponseEntity<Persoon> updatePersoon(@Valid @RequestBody Persoon persoon) throws URISyntaxException {
        log.debug("REST request to update Persoon : {}", persoon);
        if (persoon.getId() == null) {
            return createPersoon(persoon);
        }
        Persoon result = persoonService.save(persoon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, persoon.getId().toString()))
            .body(result);
    }

    /**
     * GET  /persoons : get all the persoons.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of persoons in body
     */
    @GetMapping("/persoons")
    @Timed
    public ResponseEntity<List<Persoon>> getAllPersoons(PersoonCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Persoons by criteria: {}", criteria);
        Page<Persoon> page = persoonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/persoons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /persoons/:id : get the "id" persoon.
     *
     * @param id the id of the persoon to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the persoon, or with status 404 (Not Found)
     */
    @GetMapping("/persoons/{id}")
    @Timed
    public ResponseEntity<Persoon> getPersoon(@PathVariable Long id) {
        log.debug("REST request to get Persoon : {}", id);
        Persoon persoon = persoonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(persoon));
    }

    /**
     * DELETE  /persoons/:id : delete the "id" persoon.
     *
     * @param id the id of the persoon to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/persoons/{id}")
    @Timed
    public ResponseEntity<Void> deletePersoon(@PathVariable Long id) {
        log.debug("REST request to delete Persoon : {}", id);
        persoonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/persoons?query=:query : search for the persoon corresponding
     * to the query.
     *
     * @param query the query of the persoon search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/persoons")
    @Timed
    public ResponseEntity<List<Persoon>> searchPersoons(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Persoons for query {}", query);
        Page<Persoon> page = persoonService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/persoons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
