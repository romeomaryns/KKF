package eu.mayrns.romeo.kkf.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.mayrns.romeo.kkf.domain.Contact;
import eu.mayrns.romeo.kkf.service.ContactService;
import eu.mayrns.romeo.kkf.web.rest.errors.BadRequestAlertException;
import eu.mayrns.romeo.kkf.web.rest.util.HeaderUtil;
import eu.mayrns.romeo.kkf.web.rest.util.PaginationUtil;
import eu.mayrns.romeo.kkf.service.dto.ContactCriteria;
import eu.mayrns.romeo.kkf.service.ContactQueryService;
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
 * REST controller for managing Contact.
 */
@RestController
@RequestMapping("/api")
public class ContactResource {

    private final Logger log = LoggerFactory.getLogger(ContactResource.class);

    private static final String ENTITY_NAME = "contact";

    private final ContactService contactService;

    private final ContactQueryService contactQueryService;

    public ContactResource(ContactService contactService, ContactQueryService contactQueryService) {
        this.contactService = contactService;
        this.contactQueryService = contactQueryService;
    }

    /**
     * POST  /contacts : Create a new contact.
     *
     * @param contact the contact to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contact, or with status 400 (Bad Request) if the contact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contacts")
    @Timed
    public ResponseEntity<Contact> createContact(@Valid @RequestBody Contact contact) throws URISyntaxException {
        log.debug("REST request to save Contact : {}", contact);
        if (contact.getId() != null) {
            throw new BadRequestAlertException("A new contact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contact result = contactService.save(contact);
        return ResponseEntity.created(new URI("/api/contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contacts : Updates an existing contact.
     *
     * @param contact the contact to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contact,
     * or with status 400 (Bad Request) if the contact is not valid,
     * or with status 500 (Internal Server Error) if the contact couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contacts")
    @Timed
    public ResponseEntity<Contact> updateContact(@Valid @RequestBody Contact contact) throws URISyntaxException {
        log.debug("REST request to update Contact : {}", contact);
        if (contact.getId() == null) {
            return createContact(contact);
        }
        Contact result = contactService.save(contact);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contact.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contacts : get all the contacts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @GetMapping("/contacts")
    @Timed
    public ResponseEntity<List<Contact>> getAllContacts(ContactCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Contacts by criteria: {}", criteria);
        Page<Contact> page = contactQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contacts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contacts/:id : get the "id" contact.
     *
     * @param id the id of the contact to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contact, or with status 404 (Not Found)
     */
    @GetMapping("/contacts/{id}")
    @Timed
    public ResponseEntity<Contact> getContact(@PathVariable Long id) {
        log.debug("REST request to get Contact : {}", id);
        Contact contact = contactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contact));
    }

    /**
     * DELETE  /contacts/:id : delete the "id" contact.
     *
     * @param id the id of the contact to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contacts/{id}")
    @Timed
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.debug("REST request to delete Contact : {}", id);
        contactService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contacts?query=:query : search for the contact corresponding
     * to the query.
     *
     * @param query the query of the contact search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/contacts")
    @Timed
    public ResponseEntity<List<Contact>> searchContacts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Contacts for query {}", query);
        Page<Contact> page = contactService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/contacts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
