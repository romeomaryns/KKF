package eu.mayrns.romeo.kkf.web.rest;

import eu.mayrns.romeo.kkf.KkfApp;

import eu.mayrns.romeo.kkf.domain.Relatie;
import eu.mayrns.romeo.kkf.domain.RelatieType;
import eu.mayrns.romeo.kkf.domain.Persoon;
import eu.mayrns.romeo.kkf.repository.RelatieRepository;
import eu.mayrns.romeo.kkf.service.RelatieService;
import eu.mayrns.romeo.kkf.repository.search.RelatieSearchRepository;
import eu.mayrns.romeo.kkf.web.rest.errors.ExceptionTranslator;
import eu.mayrns.romeo.kkf.service.dto.RelatieCriteria;
import eu.mayrns.romeo.kkf.service.RelatieQueryService;

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
 * Test class for the RelatieResource REST controller.
 *
 * @see RelatieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KkfApp.class)
public class RelatieResourceIntTest {

    private static final String DEFAULT_OMSCHRIJVING = "AAAAAAAAAA";
    private static final String UPDATED_OMSCHRIJVING = "BBBBBBBBBB";

    @Autowired
    private RelatieRepository relatieRepository;

    @Autowired
    private RelatieService relatieService;

    @Autowired
    private RelatieSearchRepository relatieSearchRepository;

    @Autowired
    private RelatieQueryService relatieQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRelatieMockMvc;

    private Relatie relatie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RelatieResource relatieResource = new RelatieResource(relatieService, relatieQueryService);
        this.restRelatieMockMvc = MockMvcBuilders.standaloneSetup(relatieResource)
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
    public static Relatie createEntity(EntityManager em) {
        Relatie relatie = new Relatie()
            .omschrijving(DEFAULT_OMSCHRIJVING);
        return relatie;
    }

    @Before
    public void initTest() {
        relatieSearchRepository.deleteAll();
        relatie = createEntity(em);
    }

    @Test
    @Transactional
    public void createRelatie() throws Exception {
        int databaseSizeBeforeCreate = relatieRepository.findAll().size();

        // Create the Relatie
        restRelatieMockMvc.perform(post("/api/relaties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatie)))
            .andExpect(status().isCreated());

        // Validate the Relatie in the database
        List<Relatie> relatieList = relatieRepository.findAll();
        assertThat(relatieList).hasSize(databaseSizeBeforeCreate + 1);
        Relatie testRelatie = relatieList.get(relatieList.size() - 1);
        assertThat(testRelatie.getOmschrijving()).isEqualTo(DEFAULT_OMSCHRIJVING);

        // Validate the Relatie in Elasticsearch
        Relatie relatieEs = relatieSearchRepository.findOne(testRelatie.getId());
        assertThat(relatieEs).isEqualToIgnoringGivenFields(testRelatie);
    }

    @Test
    @Transactional
    public void createRelatieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = relatieRepository.findAll().size();

        // Create the Relatie with an existing ID
        relatie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelatieMockMvc.perform(post("/api/relaties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatie)))
            .andExpect(status().isBadRequest());

        // Validate the Relatie in the database
        List<Relatie> relatieList = relatieRepository.findAll();
        assertThat(relatieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOmschrijvingIsRequired() throws Exception {
        int databaseSizeBeforeTest = relatieRepository.findAll().size();
        // set the field null
        relatie.setOmschrijving(null);

        // Create the Relatie, which fails.

        restRelatieMockMvc.perform(post("/api/relaties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatie)))
            .andExpect(status().isBadRequest());

        List<Relatie> relatieList = relatieRepository.findAll();
        assertThat(relatieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRelaties() throws Exception {
        // Initialize the database
        relatieRepository.saveAndFlush(relatie);

        // Get all the relatieList
        restRelatieMockMvc.perform(get("/api/relaties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatie.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    @Test
    @Transactional
    public void getRelatie() throws Exception {
        // Initialize the database
        relatieRepository.saveAndFlush(relatie);

        // Get the relatie
        restRelatieMockMvc.perform(get("/api/relaties/{id}", relatie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(relatie.getId().intValue()))
            .andExpect(jsonPath("$.omschrijving").value(DEFAULT_OMSCHRIJVING.toString()));
    }

    @Test
    @Transactional
    public void getAllRelatiesByOmschrijvingIsEqualToSomething() throws Exception {
        // Initialize the database
        relatieRepository.saveAndFlush(relatie);

        // Get all the relatieList where omschrijving equals to DEFAULT_OMSCHRIJVING
        defaultRelatieShouldBeFound("omschrijving.equals=" + DEFAULT_OMSCHRIJVING);

        // Get all the relatieList where omschrijving equals to UPDATED_OMSCHRIJVING
        defaultRelatieShouldNotBeFound("omschrijving.equals=" + UPDATED_OMSCHRIJVING);
    }

    @Test
    @Transactional
    public void getAllRelatiesByOmschrijvingIsInShouldWork() throws Exception {
        // Initialize the database
        relatieRepository.saveAndFlush(relatie);

        // Get all the relatieList where omschrijving in DEFAULT_OMSCHRIJVING or UPDATED_OMSCHRIJVING
        defaultRelatieShouldBeFound("omschrijving.in=" + DEFAULT_OMSCHRIJVING + "," + UPDATED_OMSCHRIJVING);

        // Get all the relatieList where omschrijving equals to UPDATED_OMSCHRIJVING
        defaultRelatieShouldNotBeFound("omschrijving.in=" + UPDATED_OMSCHRIJVING);
    }

    @Test
    @Transactional
    public void getAllRelatiesByOmschrijvingIsNullOrNotNull() throws Exception {
        // Initialize the database
        relatieRepository.saveAndFlush(relatie);

        // Get all the relatieList where omschrijving is not null
        defaultRelatieShouldBeFound("omschrijving.specified=true");

        // Get all the relatieList where omschrijving is null
        defaultRelatieShouldNotBeFound("omschrijving.specified=false");
    }

    @Test
    @Transactional
    public void getAllRelatiesByRelatieTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        RelatieType relatieType = RelatieTypeResourceIntTest.createEntity(em);
        em.persist(relatieType);
        em.flush();
        relatie.setRelatieType(relatieType);
        relatieRepository.saveAndFlush(relatie);
        Long relatieTypeId = relatieType.getId();

        // Get all the relatieList where relatieType equals to relatieTypeId
        defaultRelatieShouldBeFound("relatieTypeId.equals=" + relatieTypeId);

        // Get all the relatieList where relatieType equals to relatieTypeId + 1
        defaultRelatieShouldNotBeFound("relatieTypeId.equals=" + (relatieTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllRelatiesByPersonenIsEqualToSomething() throws Exception {
        // Initialize the database
        Persoon personen = PersoonResourceIntTest.createEntity(em);
        em.persist(personen);
        em.flush();
        relatie.addPersonen(personen);
        relatieRepository.saveAndFlush(relatie);
        Long personenId = personen.getId();

        // Get all the relatieList where personen equals to personenId
        defaultRelatieShouldBeFound("personenId.equals=" + personenId);

        // Get all the relatieList where personen equals to personenId + 1
        defaultRelatieShouldNotBeFound("personenId.equals=" + (personenId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRelatieShouldBeFound(String filter) throws Exception {
        restRelatieMockMvc.perform(get("/api/relaties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatie.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRelatieShouldNotBeFound(String filter) throws Exception {
        restRelatieMockMvc.perform(get("/api/relaties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingRelatie() throws Exception {
        // Get the relatie
        restRelatieMockMvc.perform(get("/api/relaties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRelatie() throws Exception {
        // Initialize the database
        relatieService.save(relatie);

        int databaseSizeBeforeUpdate = relatieRepository.findAll().size();

        // Update the relatie
        Relatie updatedRelatie = relatieRepository.findOne(relatie.getId());
        // Disconnect from session so that the updates on updatedRelatie are not directly saved in db
        em.detach(updatedRelatie);
        updatedRelatie
            .omschrijving(UPDATED_OMSCHRIJVING);

        restRelatieMockMvc.perform(put("/api/relaties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRelatie)))
            .andExpect(status().isOk());

        // Validate the Relatie in the database
        List<Relatie> relatieList = relatieRepository.findAll();
        assertThat(relatieList).hasSize(databaseSizeBeforeUpdate);
        Relatie testRelatie = relatieList.get(relatieList.size() - 1);
        assertThat(testRelatie.getOmschrijving()).isEqualTo(UPDATED_OMSCHRIJVING);

        // Validate the Relatie in Elasticsearch
        Relatie relatieEs = relatieSearchRepository.findOne(testRelatie.getId());
        assertThat(relatieEs).isEqualToIgnoringGivenFields(testRelatie);
    }

    @Test
    @Transactional
    public void updateNonExistingRelatie() throws Exception {
        int databaseSizeBeforeUpdate = relatieRepository.findAll().size();

        // Create the Relatie

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRelatieMockMvc.perform(put("/api/relaties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatie)))
            .andExpect(status().isCreated());

        // Validate the Relatie in the database
        List<Relatie> relatieList = relatieRepository.findAll();
        assertThat(relatieList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRelatie() throws Exception {
        // Initialize the database
        relatieService.save(relatie);

        int databaseSizeBeforeDelete = relatieRepository.findAll().size();

        // Get the relatie
        restRelatieMockMvc.perform(delete("/api/relaties/{id}", relatie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean relatieExistsInEs = relatieSearchRepository.exists(relatie.getId());
        assertThat(relatieExistsInEs).isFalse();

        // Validate the database is empty
        List<Relatie> relatieList = relatieRepository.findAll();
        assertThat(relatieList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRelatie() throws Exception {
        // Initialize the database
        relatieService.save(relatie);

        // Search the relatie
        restRelatieMockMvc.perform(get("/api/_search/relaties?query=id:" + relatie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatie.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Relatie.class);
        Relatie relatie1 = new Relatie();
        relatie1.setId(1L);
        Relatie relatie2 = new Relatie();
        relatie2.setId(relatie1.getId());
        assertThat(relatie1).isEqualTo(relatie2);
        relatie2.setId(2L);
        assertThat(relatie1).isNotEqualTo(relatie2);
        relatie1.setId(null);
        assertThat(relatie1).isNotEqualTo(relatie2);
    }
}
