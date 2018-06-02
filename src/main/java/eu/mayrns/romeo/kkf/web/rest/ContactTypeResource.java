package eu.mayrns.romeo.kkf.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.mayrns.romeo.kkf.domain.ContactType;
import eu.mayrns.romeo.kkf.service.ContactTypeService;
import eu.mayrns.romeo.kkf.web.rest.errors.BadRequestAlertException;
import eu.mayrns.romeo.kkf.web.rest.util.HeaderUtil;
import eu.mayrns.romeo.kkf.web.rest.util.PaginationUtil;
import eu.mayrns.romeo.kkf.service.dto.ContactTypeCriteria;
import eu.mayrns.romeo.kkf.service.ContactTypeQueryService;
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
 * REST controller for managing ContactType.
 */
@RestController
@RequestMapping("/api")
public class ContactTypeResource {

    private final Logger log = LoggerFactory.getLogger(ContactTypeResource.class);

    private static final String ENTITY_NAME = "contactType";

    private final ContactTypeService contactTypeService;

    private final ContactTypeQueryService contactTypeQueryService;

    public ContactTypeResource(ContactTypeService contactTypeService, ContactTypeQueryService contactTypeQueryService) {
        this.contactTypeService = contactTypeService;
        this.contactTypeQueryService = contactTypeQueryService;
    }

    /**
     * POST  /contact-types : Create a new contactType.
     *
     * @param contactType the contactType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactType, or with status 400 (Bad Request) if the contactType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contact-types")
    @Timed
    public ResponseEntity<ContactType> createContactType(@Valid @RequestBody ContactType contactType) throws URISyntaxException {
        log.debug("REST request to save ContactType : {}", contactType);
        if (contactType.getId() != null) {
            throw new BadRequestAlertException("A new contactType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactType result = contactTypeService.save(contactType);
        return ResponseEntity.created(new URI("/api/contact-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contact-types : Updates an existing contactType.
     *
     * @param contactType the contactType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactType,
     * or with status 400 (Bad Request) if the contactType is not valid,
     * or with status 500 (Internal Server Error) if the contactType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contact-types")
    @Timed
    public ResponseEntity<ContactType> updateContactType(@Valid @RequestBody ContactType contactType) throws URISyntaxException {
        log.debug("REST request to update ContactType : {}", contactType);
        if (contactType.getId() == null) {
            return createContactType(contactType);
        }
        ContactType result = contactTypeService.save(contactType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contact-types : get all the contactTypes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of contactTypes in body
     */
    @GetMapping("/contact-types")
    @Timed
    public ResponseEntity<List<ContactType>> getAllContactTypes(ContactTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContactTypes by criteria: {}", criteria);
        Page<ContactType> page = contactTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contact-types/:id : get the "id" contactType.
     *
     * @param id the id of the contactType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactType, or with status 404 (Not Found)
     */
    @GetMapping("/contact-types/{id}")
    @Timed
    public ResponseEntity<ContactType> getContactType(@PathVariable Long id) {
        log.debug("REST request to get ContactType : {}", id);
        ContactType contactType = contactTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contactType));
    }

    /**
     * DELETE  /contact-types/:id : delete the "id" contactType.
     *
     * @param id the id of the contactType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contact-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteContactType(@PathVariable Long id) {
        log.debug("REST request to delete ContactType : {}", id);
        contactTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contact-types?query=:query : search for the contactType corresponding
     * to the query.
     *
     * @param query the query of the contactType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/contact-types")
    @Timed
    public ResponseEntity<List<ContactType>> searchContactTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ContactTypes for query {}", query);
        Page<ContactType> page = contactTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/contact-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
