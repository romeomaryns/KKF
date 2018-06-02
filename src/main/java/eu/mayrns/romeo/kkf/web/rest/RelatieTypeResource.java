package eu.mayrns.romeo.kkf.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.mayrns.romeo.kkf.domain.RelatieType;
import eu.mayrns.romeo.kkf.service.RelatieTypeService;
import eu.mayrns.romeo.kkf.web.rest.errors.BadRequestAlertException;
import eu.mayrns.romeo.kkf.web.rest.util.HeaderUtil;
import eu.mayrns.romeo.kkf.web.rest.util.PaginationUtil;
import eu.mayrns.romeo.kkf.service.dto.RelatieTypeCriteria;
import eu.mayrns.romeo.kkf.service.RelatieTypeQueryService;
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
 * REST controller for managing RelatieType.
 */
@RestController
@RequestMapping("/api")
public class RelatieTypeResource {

    private final Logger log = LoggerFactory.getLogger(RelatieTypeResource.class);

    private static final String ENTITY_NAME = "relatieType";

    private final RelatieTypeService relatieTypeService;

    private final RelatieTypeQueryService relatieTypeQueryService;

    public RelatieTypeResource(RelatieTypeService relatieTypeService, RelatieTypeQueryService relatieTypeQueryService) {
        this.relatieTypeService = relatieTypeService;
        this.relatieTypeQueryService = relatieTypeQueryService;
    }

    /**
     * POST  /relatie-types : Create a new relatieType.
     *
     * @param relatieType the relatieType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new relatieType, or with status 400 (Bad Request) if the relatieType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/relatie-types")
    @Timed
    public ResponseEntity<RelatieType> createRelatieType(@Valid @RequestBody RelatieType relatieType) throws URISyntaxException {
        log.debug("REST request to save RelatieType : {}", relatieType);
        if (relatieType.getId() != null) {
            throw new BadRequestAlertException("A new relatieType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RelatieType result = relatieTypeService.save(relatieType);
        return ResponseEntity.created(new URI("/api/relatie-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /relatie-types : Updates an existing relatieType.
     *
     * @param relatieType the relatieType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated relatieType,
     * or with status 400 (Bad Request) if the relatieType is not valid,
     * or with status 500 (Internal Server Error) if the relatieType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/relatie-types")
    @Timed
    public ResponseEntity<RelatieType> updateRelatieType(@Valid @RequestBody RelatieType relatieType) throws URISyntaxException {
        log.debug("REST request to update RelatieType : {}", relatieType);
        if (relatieType.getId() == null) {
            return createRelatieType(relatieType);
        }
        RelatieType result = relatieTypeService.save(relatieType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, relatieType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /relatie-types : get all the relatieTypes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of relatieTypes in body
     */
    @GetMapping("/relatie-types")
    @Timed
    public ResponseEntity<List<RelatieType>> getAllRelatieTypes(RelatieTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RelatieTypes by criteria: {}", criteria);
        Page<RelatieType> page = relatieTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/relatie-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /relatie-types/:id : get the "id" relatieType.
     *
     * @param id the id of the relatieType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the relatieType, or with status 404 (Not Found)
     */
    @GetMapping("/relatie-types/{id}")
    @Timed
    public ResponseEntity<RelatieType> getRelatieType(@PathVariable Long id) {
        log.debug("REST request to get RelatieType : {}", id);
        RelatieType relatieType = relatieTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(relatieType));
    }

    /**
     * DELETE  /relatie-types/:id : delete the "id" relatieType.
     *
     * @param id the id of the relatieType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/relatie-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteRelatieType(@PathVariable Long id) {
        log.debug("REST request to delete RelatieType : {}", id);
        relatieTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/relatie-types?query=:query : search for the relatieType corresponding
     * to the query.
     *
     * @param query the query of the relatieType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/relatie-types")
    @Timed
    public ResponseEntity<List<RelatieType>> searchRelatieTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RelatieTypes for query {}", query);
        Page<RelatieType> page = relatieTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/relatie-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
