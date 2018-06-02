package eu.mayrns.romeo.kkf.web.rest;

import eu.mayrns.romeo.kkf.KkfApp;

import eu.mayrns.romeo.kkf.domain.Persoon;
import eu.mayrns.romeo.kkf.domain.Contact;
import eu.mayrns.romeo.kkf.domain.Geslacht;
import eu.mayrns.romeo.kkf.domain.Adres;
import eu.mayrns.romeo.kkf.domain.Relatie;
import eu.mayrns.romeo.kkf.domain.Contact;
import eu.mayrns.romeo.kkf.repository.PersoonRepository;
import eu.mayrns.romeo.kkf.service.PersoonService;
import eu.mayrns.romeo.kkf.repository.search.PersoonSearchRepository;
import eu.mayrns.romeo.kkf.web.rest.errors.ExceptionTranslator;
import eu.mayrns.romeo.kkf.service.dto.PersoonCriteria;
import eu.mayrns.romeo.kkf.service.PersoonQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static eu.mayrns.romeo.kkf.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PersoonResource REST controller.
 *
 * @see PersoonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KkfApp.class)
public class PersoonResourceIntTest {

    private static final String DEFAULT_VOORNAAM = "AAAAAAAAAA";
    private static final String UPDATED_VOORNAAM = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILIENAAM = "AAAAAAAAAA";
    private static final String UPDATED_FAMILIENAAM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_GEBOORTEDATUM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_GEBOORTEDATUM = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PersoonRepository persoonRepository;

    @Autowired
    private PersoonService persoonService;

    @Autowired
    private PersoonSearchRepository persoonSearchRepository;

    @Autowired
    private PersoonQueryService persoonQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPersoonMockMvc;

    private Persoon persoon;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PersoonResource persoonResource = new PersoonResource(persoonService, persoonQueryService);
        this.restPersoonMockMvc = MockMvcBuilders.standaloneSetup(persoonResource)
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
    public static Persoon createEntity(EntityManager em) {
        Persoon persoon = new Persoon()
            .voornaam(DEFAULT_VOORNAAM)
            .familienaam(DEFAULT_FAMILIENAAM)
            .geboortedatum(DEFAULT_GEBOORTEDATUM);
        return persoon;
    }

    @Before
    public void initTest() {
        persoonSearchRepository.deleteAll();
        persoon = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersoon() throws Exception {
        int databaseSizeBeforeCreate = persoonRepository.findAll().size();

        // Create the Persoon
        restPersoonMockMvc.perform(post("/api/persoons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persoon)))
            .andExpect(status().isCreated());

        // Validate the Persoon in the database
        List<Persoon> persoonList = persoonRepository.findAll();
        assertThat(persoonList).hasSize(databaseSizeBeforeCreate + 1);
        Persoon testPersoon = persoonList.get(persoonList.size() - 1);
        assertThat(testPersoon.getVoornaam()).isEqualTo(DEFAULT_VOORNAAM);
        assertThat(testPersoon.getFamilienaam()).isEqualTo(DEFAULT_FAMILIENAAM);
        assertThat(testPersoon.getGeboortedatum()).isEqualTo(DEFAULT_GEBOORTEDATUM);

        // Validate the Persoon in Elasticsearch
        Persoon persoonEs = persoonSearchRepository.findOne(testPersoon.getId());
        assertThat(persoonEs).isEqualToIgnoringGivenFields(testPersoon);
    }

    @Test
    @Transactional
    public void createPersoonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = persoonRepository.findAll().size();

        // Create the Persoon with an existing ID
        persoon.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersoonMockMvc.perform(post("/api/persoons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persoon)))
            .andExpect(status().isBadRequest());

        // Validate the Persoon in the database
        List<Persoon> persoonList = persoonRepository.findAll();
        assertThat(persoonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkVoornaamIsRequired() throws Exception {
        int databaseSizeBeforeTest = persoonRepository.findAll().size();
        // set the field null
        persoon.setVoornaam(null);

        // Create the Persoon, which fails.

        restPersoonMockMvc.perform(post("/api/persoons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persoon)))
            .andExpect(status().isBadRequest());

        List<Persoon> persoonList = persoonRepository.findAll();
        assertThat(persoonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFamilienaamIsRequired() throws Exception {
        int databaseSizeBeforeTest = persoonRepository.findAll().size();
        // set the field null
        persoon.setFamilienaam(null);

        // Create the Persoon, which fails.

        restPersoonMockMvc.perform(post("/api/persoons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persoon)))
            .andExpect(status().isBadRequest());

        List<Persoon> persoonList = persoonRepository.findAll();
        assertThat(persoonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPersoons() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList
        restPersoonMockMvc.perform(get("/api/persoons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persoon.getId().intValue())))
            .andExpect(jsonPath("$.[*].voornaam").value(hasItem(DEFAULT_VOORNAAM.toString())))
            .andExpect(jsonPath("$.[*].familienaam").value(hasItem(DEFAULT_FAMILIENAAM.toString())))
            .andExpect(jsonPath("$.[*].geboortedatum").value(hasItem(DEFAULT_GEBOORTEDATUM.toString())));
    }

    @Test
    @Transactional
    public void getPersoon() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get the persoon
        restPersoonMockMvc.perform(get("/api/persoons/{id}", persoon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(persoon.getId().intValue()))
            .andExpect(jsonPath("$.voornaam").value(DEFAULT_VOORNAAM.toString()))
            .andExpect(jsonPath("$.familienaam").value(DEFAULT_FAMILIENAAM.toString()))
            .andExpect(jsonPath("$.geboortedatum").value(DEFAULT_GEBOORTEDATUM.toString()));
    }

    @Test
    @Transactional
    public void getAllPersoonsByVoornaamIsEqualToSomething() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList where voornaam equals to DEFAULT_VOORNAAM
        defaultPersoonShouldBeFound("voornaam.equals=" + DEFAULT_VOORNAAM);

        // Get all the persoonList where voornaam equals to UPDATED_VOORNAAM
        defaultPersoonShouldNotBeFound("voornaam.equals=" + UPDATED_VOORNAAM);
    }

    @Test
    @Transactional
    public void getAllPersoonsByVoornaamIsInShouldWork() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList where voornaam in DEFAULT_VOORNAAM or UPDATED_VOORNAAM
        defaultPersoonShouldBeFound("voornaam.in=" + DEFAULT_VOORNAAM + "," + UPDATED_VOORNAAM);

        // Get all the persoonList where voornaam equals to UPDATED_VOORNAAM
        defaultPersoonShouldNotBeFound("voornaam.in=" + UPDATED_VOORNAAM);
    }

    @Test
    @Transactional
    public void getAllPersoonsByVoornaamIsNullOrNotNull() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList where voornaam is not null
        defaultPersoonShouldBeFound("voornaam.specified=true");

        // Get all the persoonList where voornaam is null
        defaultPersoonShouldNotBeFound("voornaam.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersoonsByFamilienaamIsEqualToSomething() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList where familienaam equals to DEFAULT_FAMILIENAAM
        defaultPersoonShouldBeFound("familienaam.equals=" + DEFAULT_FAMILIENAAM);

        // Get all the persoonList where familienaam equals to UPDATED_FAMILIENAAM
        defaultPersoonShouldNotBeFound("familienaam.equals=" + UPDATED_FAMILIENAAM);
    }

    @Test
    @Transactional
    public void getAllPersoonsByFamilienaamIsInShouldWork() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList where familienaam in DEFAULT_FAMILIENAAM or UPDATED_FAMILIENAAM
        defaultPersoonShouldBeFound("familienaam.in=" + DEFAULT_FAMILIENAAM + "," + UPDATED_FAMILIENAAM);

        // Get all the persoonList where familienaam equals to UPDATED_FAMILIENAAM
        defaultPersoonShouldNotBeFound("familienaam.in=" + UPDATED_FAMILIENAAM);
    }

    @Test
    @Transactional
    public void getAllPersoonsByFamilienaamIsNullOrNotNull() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList where familienaam is not null
        defaultPersoonShouldBeFound("familienaam.specified=true");

        // Get all the persoonList where familienaam is null
        defaultPersoonShouldNotBeFound("familienaam.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersoonsByGeboortedatumIsEqualToSomething() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList where geboortedatum equals to DEFAULT_GEBOORTEDATUM
        defaultPersoonShouldBeFound("geboortedatum.equals=" + DEFAULT_GEBOORTEDATUM);

        // Get all the persoonList where geboortedatum equals to UPDATED_GEBOORTEDATUM
        defaultPersoonShouldNotBeFound("geboortedatum.equals=" + UPDATED_GEBOORTEDATUM);
    }

    @Test
    @Transactional
    public void getAllPersoonsByGeboortedatumIsInShouldWork() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList where geboortedatum in DEFAULT_GEBOORTEDATUM or UPDATED_GEBOORTEDATUM
        defaultPersoonShouldBeFound("geboortedatum.in=" + DEFAULT_GEBOORTEDATUM + "," + UPDATED_GEBOORTEDATUM);

        // Get all the persoonList where geboortedatum equals to UPDATED_GEBOORTEDATUM
        defaultPersoonShouldNotBeFound("geboortedatum.in=" + UPDATED_GEBOORTEDATUM);
    }

    @Test
    @Transactional
    public void getAllPersoonsByGeboortedatumIsNullOrNotNull() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList where geboortedatum is not null
        defaultPersoonShouldBeFound("geboortedatum.specified=true");

        // Get all the persoonList where geboortedatum is null
        defaultPersoonShouldNotBeFound("geboortedatum.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersoonsByGeboortedatumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList where geboortedatum greater than or equals to DEFAULT_GEBOORTEDATUM
        defaultPersoonShouldBeFound("geboortedatum.greaterOrEqualThan=" + DEFAULT_GEBOORTEDATUM);

        // Get all the persoonList where geboortedatum greater than or equals to UPDATED_GEBOORTEDATUM
        defaultPersoonShouldNotBeFound("geboortedatum.greaterOrEqualThan=" + UPDATED_GEBOORTEDATUM);
    }

    @Test
    @Transactional
    public void getAllPersoonsByGeboortedatumIsLessThanSomething() throws Exception {
        // Initialize the database
        persoonRepository.saveAndFlush(persoon);

        // Get all the persoonList where geboortedatum less than or equals to DEFAULT_GEBOORTEDATUM
        defaultPersoonShouldNotBeFound("geboortedatum.lessThan=" + DEFAULT_GEBOORTEDATUM);

        // Get all the persoonList where geboortedatum less than or equals to UPDATED_GEBOORTEDATUM
        defaultPersoonShouldBeFound("geboortedatum.lessThan=" + UPDATED_GEBOORTEDATUM);
    }


    @Test
    @Transactional
    public void getAllPersoonsByContactInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        Contact contactInfo = ContactResourceIntTest.createEntity(em);
        em.persist(contactInfo);
        em.flush();
        persoon.setContactInfo(contactInfo);
        persoonRepository.saveAndFlush(persoon);
        Long contactInfoId = contactInfo.getId();

        // Get all the persoonList where contactInfo equals to contactInfoId
        defaultPersoonShouldBeFound("contactInfoId.equals=" + contactInfoId);

        // Get all the persoonList where contactInfo equals to contactInfoId + 1
        defaultPersoonShouldNotBeFound("contactInfoId.equals=" + (contactInfoId + 1));
    }


    @Test
    @Transactional
    public void getAllPersoonsByGeslachtIsEqualToSomething() throws Exception {
        // Initialize the database
        Geslacht geslacht = GeslachtResourceIntTest.createEntity(em);
        em.persist(geslacht);
        em.flush();
        persoon.setGeslacht(geslacht);
        persoonRepository.saveAndFlush(persoon);
        Long geslachtId = geslacht.getId();

        // Get all the persoonList where geslacht equals to geslachtId
        defaultPersoonShouldBeFound("geslachtId.equals=" + geslachtId);

        // Get all the persoonList where geslacht equals to geslachtId + 1
        defaultPersoonShouldNotBeFound("geslachtId.equals=" + (geslachtId + 1));
    }


    @Test
    @Transactional
    public void getAllPersoonsByAdresInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        Adres adresInfo = AdresResourceIntTest.createEntity(em);
        em.persist(adresInfo);
        em.flush();
        persoon.setAdresInfo(adresInfo);
        persoonRepository.saveAndFlush(persoon);
        Long adresInfoId = adresInfo.getId();

        // Get all the persoonList where adresInfo equals to adresInfoId
        defaultPersoonShouldBeFound("adresInfoId.equals=" + adresInfoId);

        // Get all the persoonList where adresInfo equals to adresInfoId + 1
        defaultPersoonShouldNotBeFound("adresInfoId.equals=" + (adresInfoId + 1));
    }


    @Test
    @Transactional
    public void getAllPersoonsByRelatiesIsEqualToSomething() throws Exception {
        // Initialize the database
        Relatie relaties = RelatieResourceIntTest.createEntity(em);
        em.persist(relaties);
        em.flush();
        persoon.addRelaties(relaties);
        persoonRepository.saveAndFlush(persoon);
        Long relatiesId = relaties.getId();

        // Get all the persoonList where relaties equals to relatiesId
        defaultPersoonShouldBeFound("relatiesId.equals=" + relatiesId);

        // Get all the persoonList where relaties equals to relatiesId + 1
        defaultPersoonShouldNotBeFound("relatiesId.equals=" + (relatiesId + 1));
    }


    @Test
    @Transactional
    public void getAllPersoonsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        Contact contact = ContactResourceIntTest.createEntity(em);
        em.persist(contact);
        em.flush();
        persoon.setContact(contact);
        persoonRepository.saveAndFlush(persoon);
        Long contactId = contact.getId();

        // Get all the persoonList where contact equals to contactId
        defaultPersoonShouldBeFound("contactId.equals=" + contactId);

        // Get all the persoonList where contact equals to contactId + 1
        defaultPersoonShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPersoonShouldBeFound(String filter) throws Exception {
        restPersoonMockMvc.perform(get("/api/persoons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persoon.getId().intValue())))
            .andExpect(jsonPath("$.[*].voornaam").value(hasItem(DEFAULT_VOORNAAM.toString())))
            .andExpect(jsonPath("$.[*].familienaam").value(hasItem(DEFAULT_FAMILIENAAM.toString())))
            .andExpect(jsonPath("$.[*].geboortedatum").value(hasItem(DEFAULT_GEBOORTEDATUM.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPersoonShouldNotBeFound(String filter) throws Exception {
        restPersoonMockMvc.perform(get("/api/persoons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPersoon() throws Exception {
        // Get the persoon
        restPersoonMockMvc.perform(get("/api/persoons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersoon() throws Exception {
        // Initialize the database
        persoonService.save(persoon);

        int databaseSizeBeforeUpdate = persoonRepository.findAll().size();

        // Update the persoon
        Persoon updatedPersoon = persoonRepository.findOne(persoon.getId());
        // Disconnect from session so that the updates on updatedPersoon are not directly saved in db
        em.detach(updatedPersoon);
        updatedPersoon
            .voornaam(UPDATED_VOORNAAM)
            .familienaam(UPDATED_FAMILIENAAM)
            .geboortedatum(UPDATED_GEBOORTEDATUM);

        restPersoonMockMvc.perform(put("/api/persoons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPersoon)))
            .andExpect(status().isOk());

        // Validate the Persoon in the database
        List<Persoon> persoonList = persoonRepository.findAll();
        assertThat(persoonList).hasSize(databaseSizeBeforeUpdate);
        Persoon testPersoon = persoonList.get(persoonList.size() - 1);
        assertThat(testPersoon.getVoornaam()).isEqualTo(UPDATED_VOORNAAM);
        assertThat(testPersoon.getFamilienaam()).isEqualTo(UPDATED_FAMILIENAAM);
        assertThat(testPersoon.getGeboortedatum()).isEqualTo(UPDATED_GEBOORTEDATUM);

        // Validate the Persoon in Elasticsearch
        Persoon persoonEs = persoonSearchRepository.findOne(testPersoon.getId());
        assertThat(persoonEs).isEqualToIgnoringGivenFields(testPersoon);
    }

    @Test
    @Transactional
    public void updateNonExistingPersoon() throws Exception {
        int databaseSizeBeforeUpdate = persoonRepository.findAll().size();

        // Create the Persoon

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPersoonMockMvc.perform(put("/api/persoons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(persoon)))
            .andExpect(status().isCreated());

        // Validate the Persoon in the database
        List<Persoon> persoonList = persoonRepository.findAll();
        assertThat(persoonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePersoon() throws Exception {
        // Initialize the database
        persoonService.save(persoon);

        int databaseSizeBeforeDelete = persoonRepository.findAll().size();

        // Get the persoon
        restPersoonMockMvc.perform(delete("/api/persoons/{id}", persoon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean persoonExistsInEs = persoonSearchRepository.exists(persoon.getId());
        assertThat(persoonExistsInEs).isFalse();

        // Validate the database is empty
        List<Persoon> persoonList = persoonRepository.findAll();
        assertThat(persoonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPersoon() throws Exception {
        // Initialize the database
        persoonService.save(persoon);

        // Search the persoon
        restPersoonMockMvc.perform(get("/api/_search/persoons?query=id:" + persoon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persoon.getId().intValue())))
            .andExpect(jsonPath("$.[*].voornaam").value(hasItem(DEFAULT_VOORNAAM.toString())))
            .andExpect(jsonPath("$.[*].familienaam").value(hasItem(DEFAULT_FAMILIENAAM.toString())))
            .andExpect(jsonPath("$.[*].geboortedatum").value(hasItem(DEFAULT_GEBOORTEDATUM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Persoon.class);
        Persoon persoon1 = new Persoon();
        persoon1.setId(1L);
        Persoon persoon2 = new Persoon();
        persoon2.setId(persoon1.getId());
        assertThat(persoon1).isEqualTo(persoon2);
        persoon2.setId(2L);
        assertThat(persoon1).isNotEqualTo(persoon2);
        persoon1.setId(null);
        assertThat(persoon1).isNotEqualTo(persoon2);
    }
}
