package eu.mayrns.romeo.kkf.web.rest;

import eu.mayrns.romeo.kkf.KkfApp;

import eu.mayrns.romeo.kkf.domain.Geslacht;
import eu.mayrns.romeo.kkf.repository.GeslachtRepository;
import eu.mayrns.romeo.kkf.service.GeslachtService;
import eu.mayrns.romeo.kkf.repository.search.GeslachtSearchRepository;
import eu.mayrns.romeo.kkf.web.rest.errors.ExceptionTranslator;
import eu.mayrns.romeo.kkf.service.dto.GeslachtCriteria;
import eu.mayrns.romeo.kkf.service.GeslachtQueryService;

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
 * Test class for the GeslachtResource REST controller.
 *
 * @see GeslachtResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KkfApp.class)
public class GeslachtResourceIntTest {

    private static final String DEFAULT_OMSCHRIJVING = "AAAAAAAAAA";
    private static final String UPDATED_OMSCHRIJVING = "BBBBBBBBBB";

    @Autowired
    private GeslachtRepository geslachtRepository;

    @Autowired
    private GeslachtService geslachtService;

    @Autowired
    private GeslachtSearchRepository geslachtSearchRepository;

    @Autowired
    private GeslachtQueryService geslachtQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGeslachtMockMvc;

    private Geslacht geslacht;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GeslachtResource geslachtResource = new GeslachtResource(geslachtService, geslachtQueryService);
        this.restGeslachtMockMvc = MockMvcBuilders.standaloneSetup(geslachtResource)
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
    public static Geslacht createEntity(EntityManager em) {
        Geslacht geslacht = new Geslacht()
            .omschrijving(DEFAULT_OMSCHRIJVING);
        return geslacht;
    }

    @Before
    public void initTest() {
        geslachtSearchRepository.deleteAll();
        geslacht = createEntity(em);
    }

    @Test
    @Transactional
    public void createGeslacht() throws Exception {
        int databaseSizeBeforeCreate = geslachtRepository.findAll().size();

        // Create the Geslacht
        restGeslachtMockMvc.perform(post("/api/geslachts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geslacht)))
            .andExpect(status().isCreated());

        // Validate the Geslacht in the database
        List<Geslacht> geslachtList = geslachtRepository.findAll();
        assertThat(geslachtList).hasSize(databaseSizeBeforeCreate + 1);
        Geslacht testGeslacht = geslachtList.get(geslachtList.size() - 1);
        assertThat(testGeslacht.getOmschrijving()).isEqualTo(DEFAULT_OMSCHRIJVING);

        // Validate the Geslacht in Elasticsearch
        Geslacht geslachtEs = geslachtSearchRepository.findOne(testGeslacht.getId());
        assertThat(geslachtEs).isEqualToIgnoringGivenFields(testGeslacht);
    }

    @Test
    @Transactional
    public void createGeslachtWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = geslachtRepository.findAll().size();

        // Create the Geslacht with an existing ID
        geslacht.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeslachtMockMvc.perform(post("/api/geslachts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geslacht)))
            .andExpect(status().isBadRequest());

        // Validate the Geslacht in the database
        List<Geslacht> geslachtList = geslachtRepository.findAll();
        assertThat(geslachtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOmschrijvingIsRequired() throws Exception {
        int databaseSizeBeforeTest = geslachtRepository.findAll().size();
        // set the field null
        geslacht.setOmschrijving(null);

        // Create the Geslacht, which fails.

        restGeslachtMockMvc.perform(post("/api/geslachts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geslacht)))
            .andExpect(status().isBadRequest());

        List<Geslacht> geslachtList = geslachtRepository.findAll();
        assertThat(geslachtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGeslachts() throws Exception {
        // Initialize the database
        geslachtRepository.saveAndFlush(geslacht);

        // Get all the geslachtList
        restGeslachtMockMvc.perform(get("/api/geslachts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geslacht.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    @Test
    @Transactional
    public void getGeslacht() throws Exception {
        // Initialize the database
        geslachtRepository.saveAndFlush(geslacht);

        // Get the geslacht
        restGeslachtMockMvc.perform(get("/api/geslachts/{id}", geslacht.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(geslacht.getId().intValue()))
            .andExpect(jsonPath("$.omschrijving").value(DEFAULT_OMSCHRIJVING.toString()));
    }

    @Test
    @Transactional
    public void getAllGeslachtsByOmschrijvingIsEqualToSomething() throws Exception {
        // Initialize the database
        geslachtRepository.saveAndFlush(geslacht);

        // Get all the geslachtList where omschrijving equals to DEFAULT_OMSCHRIJVING
        defaultGeslachtShouldBeFound("omschrijving.equals=" + DEFAULT_OMSCHRIJVING);

        // Get all the geslachtList where omschrijving equals to UPDATED_OMSCHRIJVING
        defaultGeslachtShouldNotBeFound("omschrijving.equals=" + UPDATED_OMSCHRIJVING);
    }

    @Test
    @Transactional
    public void getAllGeslachtsByOmschrijvingIsInShouldWork() throws Exception {
        // Initialize the database
        geslachtRepository.saveAndFlush(geslacht);

        // Get all the geslachtList where omschrijving in DEFAULT_OMSCHRIJVING or UPDATED_OMSCHRIJVING
        defaultGeslachtShouldBeFound("omschrijving.in=" + DEFAULT_OMSCHRIJVING + "," + UPDATED_OMSCHRIJVING);

        // Get all the geslachtList where omschrijving equals to UPDATED_OMSCHRIJVING
        defaultGeslachtShouldNotBeFound("omschrijving.in=" + UPDATED_OMSCHRIJVING);
    }

    @Test
    @Transactional
    public void getAllGeslachtsByOmschrijvingIsNullOrNotNull() throws Exception {
        // Initialize the database
        geslachtRepository.saveAndFlush(geslacht);

        // Get all the geslachtList where omschrijving is not null
        defaultGeslachtShouldBeFound("omschrijving.specified=true");

        // Get all the geslachtList where omschrijving is null
        defaultGeslachtShouldNotBeFound("omschrijving.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGeslachtShouldBeFound(String filter) throws Exception {
        restGeslachtMockMvc.perform(get("/api/geslachts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geslacht.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGeslachtShouldNotBeFound(String filter) throws Exception {
        restGeslachtMockMvc.perform(get("/api/geslachts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingGeslacht() throws Exception {
        // Get the geslacht
        restGeslachtMockMvc.perform(get("/api/geslachts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGeslacht() throws Exception {
        // Initialize the database
        geslachtService.save(geslacht);

        int databaseSizeBeforeUpdate = geslachtRepository.findAll().size();

        // Update the geslacht
        Geslacht updatedGeslacht = geslachtRepository.findOne(geslacht.getId());
        // Disconnect from session so that the updates on updatedGeslacht are not directly saved in db
        em.detach(updatedGeslacht);
        updatedGeslacht
            .omschrijving(UPDATED_OMSCHRIJVING);

        restGeslachtMockMvc.perform(put("/api/geslachts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGeslacht)))
            .andExpect(status().isOk());

        // Validate the Geslacht in the database
        List<Geslacht> geslachtList = geslachtRepository.findAll();
        assertThat(geslachtList).hasSize(databaseSizeBeforeUpdate);
        Geslacht testGeslacht = geslachtList.get(geslachtList.size() - 1);
        assertThat(testGeslacht.getOmschrijving()).isEqualTo(UPDATED_OMSCHRIJVING);

        // Validate the Geslacht in Elasticsearch
        Geslacht geslachtEs = geslachtSearchRepository.findOne(testGeslacht.getId());
        assertThat(geslachtEs).isEqualToIgnoringGivenFields(testGeslacht);
    }

    @Test
    @Transactional
    public void updateNonExistingGeslacht() throws Exception {
        int databaseSizeBeforeUpdate = geslachtRepository.findAll().size();

        // Create the Geslacht

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGeslachtMockMvc.perform(put("/api/geslachts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(geslacht)))
            .andExpect(status().isCreated());

        // Validate the Geslacht in the database
        List<Geslacht> geslachtList = geslachtRepository.findAll();
        assertThat(geslachtList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGeslacht() throws Exception {
        // Initialize the database
        geslachtService.save(geslacht);

        int databaseSizeBeforeDelete = geslachtRepository.findAll().size();

        // Get the geslacht
        restGeslachtMockMvc.perform(delete("/api/geslachts/{id}", geslacht.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean geslachtExistsInEs = geslachtSearchRepository.exists(geslacht.getId());
        assertThat(geslachtExistsInEs).isFalse();

        // Validate the database is empty
        List<Geslacht> geslachtList = geslachtRepository.findAll();
        assertThat(geslachtList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGeslacht() throws Exception {
        // Initialize the database
        geslachtService.save(geslacht);

        // Search the geslacht
        restGeslachtMockMvc.perform(get("/api/_search/geslachts?query=id:" + geslacht.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geslacht.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Geslacht.class);
        Geslacht geslacht1 = new Geslacht();
        geslacht1.setId(1L);
        Geslacht geslacht2 = new Geslacht();
        geslacht2.setId(geslacht1.getId());
        assertThat(geslacht1).isEqualTo(geslacht2);
        geslacht2.setId(2L);
        assertThat(geslacht1).isNotEqualTo(geslacht2);
        geslacht1.setId(null);
        assertThat(geslacht1).isNotEqualTo(geslacht2);
    }
}
