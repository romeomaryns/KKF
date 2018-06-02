package eu.mayrns.romeo.kkf.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.mayrns.romeo.kkf.domain.Relatie;
import eu.mayrns.romeo.kkf.service.RelatieService;
import eu.mayrns.romeo.kkf.web.rest.errors.BadRequestAlertException;
import eu.mayrns.romeo.kkf.web.rest.util.HeaderUtil;
import eu.mayrns.romeo.kkf.web.rest.util.PaginationUtil;
import eu.mayrns.romeo.kkf.service.dto.RelatieCriteria;
import eu.mayrns.romeo.kkf.service.RelatieQueryService;
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
 * REST controller for managing Relatie.
 */
@RestController
@RequestMapping("/api")
public class RelatieResource {

    private final Logger log = LoggerFactory.getLogger(RelatieResource.class);

    private static final String ENTITY_NAME = "relatie";

    private final RelatieService relatieService;

    private final RelatieQueryService relatieQueryService;

    public RelatieResource(RelatieService relatieService, RelatieQueryService relatieQueryService) {
        this.relatieService = relatieService;
        this.relatieQueryService = relatieQueryService;
    }

    /**
     * POST  /relaties : Create a new relatie.
     *
     * @param relatie the relatie to create
     * @return the ResponseEntity with status 201 (Created) and with body the new relatie, or with status 400 (Bad Request) if the relatie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/relaties")
    @Timed
    public ResponseEntity<Relatie> createRelatie(@Valid @RequestBody Relatie relatie) throws URISyntaxException {
        log.debug("REST request to save Relatie : {}", relatie);
        if (relatie.getId() != null) {
            throw new BadRequestAlertException("A new relatie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Relatie result = relatieService.save(relatie);
        return ResponseEntity.created(new URI("/api/relaties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /relaties : Updates an existing relatie.
     *
     * @param relatie the relatie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated relatie,
     * or with status 400 (Bad Request) if the relatie is not valid,
     * or with status 500 (Internal Server Error) if the relatie couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/relaties")
    @Timed
    public ResponseEntity<Relatie> updateRelatie(@Valid @RequestBody Relatie relatie) throws URISyntaxException {
        log.debug("REST request to update Relatie : {}", relatie);
        if (relatie.getId() == null) {
            return createRelatie(relatie);
        }
        Relatie result = relatieService.save(relatie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, relatie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /relaties : get all the relaties.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of relaties in body
     */
    @GetMapping("/relaties")
    @Timed
    public ResponseEntity<List<Relatie>> getAllRelaties(RelatieCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Relaties by criteria: {}", criteria);
        Page<Relatie> page = relatieQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/relaties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /relaties/:id : get the "id" relatie.
     *
     * @param id the id of the relatie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the relatie, or with status 404 (Not Found)
     */
    @GetMapping("/relaties/{id}")
    @Timed
    public ResponseEntity<Relatie> getRelatie(@PathVariable Long id) {
        log.debug("REST request to get Relatie : {}", id);
        Relatie relatie = relatieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(relatie));
    }

    /**
     * DELETE  /relaties/:id : delete the "id" relatie.
     *
     * @param id the id of the relatie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/relaties/{id}")
    @Timed
    public ResponseEntity<Void> deleteRelatie(@PathVariable Long id) {
        log.debug("REST request to delete Relatie : {}", id);
        relatieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/relaties?query=:query : search for the relatie corresponding
     * to the query.
     *
     * @param query the query of the relatie search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/relaties")
    @Timed
    public ResponseEntity<List<Relatie>> searchRelaties(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Relaties for query {}", query);
        Page<Relatie> page = relatieService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/relaties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
