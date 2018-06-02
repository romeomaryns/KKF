package eu.mayrns.romeo.kkf.web.rest;

import eu.mayrns.romeo.kkf.KkfApp;

import eu.mayrns.romeo.kkf.domain.ContactType;
import eu.mayrns.romeo.kkf.repository.ContactTypeRepository;
import eu.mayrns.romeo.kkf.service.ContactTypeService;
import eu.mayrns.romeo.kkf.repository.search.ContactTypeSearchRepository;
import eu.mayrns.romeo.kkf.web.rest.errors.ExceptionTranslator;
import eu.mayrns.romeo.kkf.service.dto.ContactTypeCriteria;
import eu.mayrns.romeo.kkf.service.ContactTypeQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static eu.mayrns.romeo.kkf.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContactTypeResource REST controller.
 *
 * @see ContactTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KkfApp.class)
public class ContactTypeResourceIntTest {

    private static final String DEFAULT_OMSCHRIJVING = "AAAAAAAAAA";
    private static final String UPDATED_OMSCHRIJVING = "BBBBBBBBBB";

    @Autowired
    private ContactTypeRepository contactTypeRepository;

    @Autowired
    private ContactTypeService contactTypeService;

    @Autowired
    private ContactTypeSearchRepository contactTypeSearchRepository;

    @Autowired
    private ContactTypeQueryService contactTypeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContactTypeMockMvc;

    private ContactType contactType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContactTypeResource contactTypeResource = new ContactTypeResource(contactTypeService, contactTypeQueryService);
        this.restContactTypeMockMvc = MockMvcBuilders.standaloneSetup(contactTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactType createEntity(EntityManager em) {
        ContactType contactType = new ContactType()
            .omschrijving(DEFAULT_OMSCHRIJVING);
        return contactType;
    }

    @Before
    public void initTest() {
        contactTypeSearchRepository.deleteAll();
        contactType = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactType() throws Exception {
        int databaseSizeBeforeCreate = contactTypeRepository.findAll().size();

        // Create the ContactType
        restContactTypeMockMvc.perform(post("/api/contact-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactType)))
            .andExpect(status().isCreated());

        // Validate the ContactType in the database
        List<ContactType> contactTypeList = contactTypeRepository.findAll();
        assertThat(contactTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ContactType testContactType = contactTypeList.get(contactTypeList.size() - 1);
        assertThat(testContactType.getOmschrijving()).isEqualTo(DEFAULT_OMSCHRIJVING);

        // Validate the ContactType in Elasticsearch
        ContactType contactTypeEs = contactTypeSearchRepository.findOne(testContactType.getId());
        assertThat(contactTypeEs).isEqualToIgnoringGivenFields(testContactType);
    }

    @Test
    @Transactional
    public void createContactTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactTypeRepository.findAll().size();

        // Create the ContactType with an existing ID
        contactType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactTypeMockMvc.perform(post("/api/contact-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactType)))
            .andExpect(status().isBadRequest());

        // Validate the ContactType in the database
        List<ContactType> contactTypeList = contactTypeRepository.findAll();
        assertThat(contactTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOmschrijvingIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactTypeRepository.findAll().size();
        // set the field null
        contactType.setOmschrijving(null);

        // Create the ContactType, which fails.

        restContactTypeMockMvc.perform(post("/api/contact-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactType)))
            .andExpect(status().isBadRequest());

        List<ContactType> contactTypeList = contactTypeRepository.findAll();
        assertThat(contactTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactTypes() throws Exception {
        // Initialize the database
        contactTypeRepository.saveAndFlush(contactType);

        // Get all the contactTypeList
        restContactTypeMockMvc.perform(get("/api/contact-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactType.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    @Test
    @Transactional
    public void getContactType() throws Exception {
        // Initialize the database
        contactTypeRepository.saveAndFlush(contactType);

        // Get the contactType
        restContactTypeMockMvc.perform(get("/api/contact-types/{id}", contactType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contactType.getId().intValue()))
            .andExpect(jsonPath("$.omschrijving").value(DEFAULT_OMSCHRIJVING.toString()));
    }

    @Test
    @Transactional
    public void getAllContactTypesByOmschrijvingIsEqualToSomething() throws Exception {
        // Initialize the database
        contactTypeRepository.saveAndFlush(contactType);

        // Get all the contactTypeList where omschrijving equals to DEFAULT_OMSCHRIJVING
        defaultContactTypeShouldBeFound("omschrijving.equals=" + DEFAULT_OMSCHRIJVING);

        // Get all the contactTypeList where omschrijving equals to UPDATED_OMSCHRIJVING
        defaultContactTypeShouldNotBeFound("omschrijving.equals=" + UPDATED_OMSCHRIJVING);
    }

    @Test
    @Transactional
    public void getAllContactTypesByOmschrijvingIsInShouldWork() throws Exception {
        // Initialize the database
        contactTypeRepository.saveAndFlush(contactType);

        // Get all the contactTypeList where omschrijving in DEFAULT_OMSCHRIJVING or UPDATED_OMSCHRIJVING
        defaultContactTypeShouldBeFound("omschrijving.in=" + DEFAULT_OMSCHRIJVING + "," + UPDATED_OMSCHRIJVING);

        // Get all the contactTypeList where omschrijving equals to UPDATED_OMSCHRIJVING
        defaultContactTypeShouldNotBeFound("omschrijving.in=" + UPDATED_OMSCHRIJVING);
    }

    @Test
    @Transactional
    public void getAllContactTypesByOmschrijvingIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactTypeRepository.saveAndFlush(contactType);

        // Get all the contactTypeList where omschrijving is not null
        defaultContactTypeShouldBeFound("omschrijving.specified=true");

        // Get all the contactTypeList where omschrijving is null
        defaultContactTypeShouldNotBeFound("omschrijving.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultContactTypeShouldBeFound(String filter) throws Exception {
        restContactTypeMockMvc.perform(get("/api/contact-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactType.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultContactTypeShouldNotBeFound(String filter) throws Exception {
        restContactTypeMockMvc.perform(get("/api/contact-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingContactType() throws Exception {
        // Get the contactType
        restContactTypeMockMvc.perform(get("/api/contact-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactType() throws Exception {
        // Initialize the database
        contactTypeService.save(contactType);

        int databaseSizeBeforeUpdate = contactTypeRepository.findAll().size();

        // Update the contactType
        ContactType updatedContactType = contactTypeRepository.findOne(contactType.getId());
        // Disconnect from session so that the updates on updatedContactType are not directly saved in db
        em.detach(updatedContactType);
        updatedContactType
            .omschrijving(UPDATED_OMSCHRIJVING);

        restContactTypeMockMvc.perform(put("/api/contact-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactType)))
            .andExpect(status().isOk());

        // Validate the ContactType in the database
        List<ContactType> contactTypeList = contactTypeRepository.findAll();
        assertThat(contactTypeList).hasSize(databaseSizeBeforeUpdate);
        ContactType testContactType = contactTypeList.get(contactTypeList.size() - 1);
        assertThat(testContactType.getOmschrijving()).isEqualTo(UPDATED_OMSCHRIJVING);

        // Validate the ContactType in Elasticsearch
        ContactType contactTypeEs = contactTypeSearchRepository.findOne(testContactType.getId());
        assertThat(contactTypeEs).isEqualToIgnoringGivenFields(testContactType);
    }

    @Test
    @Transactional
    public void updateNonExistingContactType() throws Exception {
        int databaseSizeBeforeUpdate = contactTypeRepository.findAll().size();

        // Create the ContactType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContactTypeMockMvc.perform(put("/api/contact-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactType)))
            .andExpect(status().isCreated());

        // Validate the ContactType in the database
        List<ContactType> contactTypeList = contactTypeRepository.findAll();
        assertThat(contactTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContactType() throws Exception {
        // Initialize the database
        contactTypeService.save(contactType);

        int databaseSizeBeforeDelete = contactTypeRepository.findAll().size();

        // Get the contactType
        restContactTypeMockMvc.perform(delete("/api/contact-types/{id}", contactType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean contactTypeExistsInEs = contactTypeSearchRepository.exists(contactType.getId());
        assertThat(contactTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<ContactType> contactTypeList = contactTypeRepository.findAll();
        assertThat(contactTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContactType() throws Exception {
        // Initialize the database
        contactTypeService.save(contactType);

        // Search the contactType
        restContactTypeMockMvc.perform(get("/api/_search/contact-types?query=id:" + contactType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactType.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactType.class);
        ContactType contactType1 = new ContactType();
        contactType1.setId(1L);
        ContactType contactType2 = new ContactType();
        contactType2.setId(contactType1.getId());
        assertThat(contactType1).isEqualTo(contactType2);
        contactType2.setId(2L);
        assertThat(contactType1).isNotEqualTo(contactType2);
        contactType1.setId(null);
        assertThat(contactType1).isNotEqualTo(contactType2);
    }
}
