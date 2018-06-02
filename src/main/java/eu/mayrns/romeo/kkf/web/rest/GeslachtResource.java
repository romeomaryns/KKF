package eu.mayrns.romeo.kkf.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.mayrns.romeo.kkf.domain.Geslacht;
import eu.mayrns.romeo.kkf.service.GeslachtService;
import eu.mayrns.romeo.kkf.web.rest.errors.BadRequestAlertException;
import eu.mayrns.romeo.kkf.web.rest.util.HeaderUtil;
import eu.mayrns.romeo.kkf.web.rest.util.PaginationUtil;
import eu.mayrns.romeo.kkf.service.dto.GeslachtCriteria;
import eu.mayrns.romeo.kkf.service.GeslachtQueryService;
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
 * REST controller for managing Geslacht.
 */
@RestController
@RequestMapping("/api")
public class GeslachtResource {

    private final Logger log = LoggerFactory.getLogger(GeslachtResource.class);

    private static final String ENTITY_NAME = "geslacht";

    private final GeslachtService geslachtService;

    private final GeslachtQueryService geslachtQueryService;

    public GeslachtResource(GeslachtService geslachtService, GeslachtQueryService geslachtQueryService) {
        this.geslachtService = geslachtService;
        this.geslachtQueryService = geslachtQueryService;
    }

    /**
     * POST  /geslachts : Create a new geslacht.
     *
     * @param geslacht the geslacht to create
     * @return the ResponseEntity with status 201 (Created) and with body the new geslacht, or with status 400 (Bad Request) if the geslacht has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/geslachts")
    @Timed
    public ResponseEntity<Geslacht> createGeslacht(@Valid @RequestBody Geslacht geslacht) throws URISyntaxException {
        log.debug("REST request to save Geslacht : {}", geslacht);
        if (geslacht.getId() != null) {
            throw new BadRequestAlertException("A new geslacht cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Geslacht result = geslachtService.save(geslacht);
        return ResponseEntity.created(new URI("/api/geslachts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /geslachts : Updates an existing geslacht.
     *
     * @param geslacht the geslacht to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated geslacht,
     * or with status 400 (Bad Request) if the geslacht is not valid,
     * or with status 500 (Internal Server Error) if the geslacht couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/geslachts")
    @Timed
    public ResponseEntity<Geslacht> updateGeslacht(@Valid @RequestBody Geslacht geslacht) throws URISyntaxException {
        log.debug("REST request to update Geslacht : {}", geslacht);
        if (geslacht.getId() == null) {
            return createGeslacht(geslacht);
        }
        Geslacht result = geslachtService.save(geslacht);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, geslacht.getId().toString()))
            .body(result);
    }

    /**
     * GET  /geslachts : get all the geslachts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of geslachts in body
     */
    @GetMapping("/geslachts")
    @Timed
    public ResponseEntity<List<Geslacht>> getAllGeslachts(GeslachtCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Geslachts by criteria: {}", criteria);
        Page<Geslacht> page = geslachtQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/geslachts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /geslachts/:id : get the "id" geslacht.
     *
     * @param id the id of the geslacht to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the geslacht, or with status 404 (Not Found)
     */
    @GetMapping("/geslachts/{id}")
    @Timed
    public ResponseEntity<Geslacht> getGeslacht(@PathVariable Long id) {
        log.debug("REST request to get Geslacht : {}", id);
        Geslacht geslacht = geslachtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(geslacht));
    }

    /**
     * DELETE  /geslachts/:id : delete the "id" geslacht.
     *
     * @param id the id of the geslacht to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/geslachts/{id}")
    @Timed
    public ResponseEntity<Void> deleteGeslacht(@PathVariable Long id) {
        log.debug("REST request to delete Geslacht : {}", id);
        geslachtService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/geslachts?query=:query : search for the geslacht corresponding
     * to the query.
     *
     * @param query the query of the geslacht search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/geslachts")
    @Timed
    public ResponseEntity<List<Geslacht>> searchGeslachts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Geslachts for query {}", query);
        Page<Geslacht> page = geslachtService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/geslachts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
