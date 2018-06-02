package eu.mayrns.romeo.kkf.web.rest;

import eu.mayrns.romeo.kkf.KkfApp;

import eu.mayrns.romeo.kkf.domain.RelatieType;
import eu.mayrns.romeo.kkf.repository.RelatieTypeRepository;
import eu.mayrns.romeo.kkf.service.RelatieTypeService;
import eu.mayrns.romeo.kkf.repository.search.RelatieTypeSearchRepository;
import eu.mayrns.romeo.kkf.web.rest.errors.ExceptionTranslator;
import eu.mayrns.romeo.kkf.service.dto.RelatieTypeCriteria;
import eu.mayrns.romeo.kkf.service.RelatieTypeQueryService;

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
 * Test class for the RelatieTypeResource REST controller.
 *
 * @see RelatieTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KkfApp.class)
public class RelatieTypeResourceIntTest {

    private static final String DEFAULT_OMSCHRIJVING = "AAAAAAAAAA";
    private static final String UPDATED_OMSCHRIJVING = "BBBBBBBBBB";

    @Autowired
    private RelatieTypeRepository relatieTypeRepository;

    @Autowired
    private RelatieTypeService relatieTypeService;

    @Autowired
    private RelatieTypeSearchRepository relatieTypeSearchRepository;

    @Autowired
    private RelatieTypeQueryService relatieTypeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRelatieTypeMockMvc;

    private RelatieType relatieType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RelatieTypeResource relatieTypeResource = new RelatieTypeResource(relatieTypeService, relatieTypeQueryService);
        this.restRelatieTypeMockMvc = MockMvcBuilders.standaloneSetup(relatieTypeResource)
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
    public static RelatieType createEntity(EntityManager em) {
        RelatieType relatieType = new RelatieType()
            .omschrijving(DEFAULT_OMSCHRIJVING);
        return relatieType;
    }

    @Before
    public void initTest() {
        relatieTypeSearchRepository.deleteAll();
        relatieType = createEntity(em);
    }

    @Test
    @Transactional
    public void createRelatieType() throws Exception {
        int databaseSizeBeforeCreate = relatieTypeRepository.findAll().size();

        // Create the RelatieType
        restRelatieTypeMockMvc.perform(post("/api/relatie-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatieType)))
            .andExpect(status().isCreated());

        // Validate the RelatieType in the database
        List<RelatieType> relatieTypeList = relatieTypeRepository.findAll();
        assertThat(relatieTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RelatieType testRelatieType = relatieTypeList.get(relatieTypeList.size() - 1);
        assertThat(testRelatieType.getOmschrijving()).isEqualTo(DEFAULT_OMSCHRIJVING);

        // Validate the RelatieType in Elasticsearch
        RelatieType relatieTypeEs = relatieTypeSearchRepository.findOne(testRelatieType.getId());
        assertThat(relatieTypeEs).isEqualToIgnoringGivenFields(testRelatieType);
    }

    @Test
    @Transactional
    public void createRelatieTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = relatieTypeRepository.findAll().size();

        // Create the RelatieType with an existing ID
        relatieType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelatieTypeMockMvc.perform(post("/api/relatie-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatieType)))
            .andExpect(status().isBadRequest());

        // Validate the RelatieType in the database
        List<RelatieType> relatieTypeList = relatieTypeRepository.findAll();
        assertThat(relatieTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOmschrijvingIsRequired() throws Exception {
        int databaseSizeBeforeTest = relatieTypeRepository.findAll().size();
        // set the field null
        relatieType.setOmschrijving(null);

        // Create the RelatieType, which fails.

        restRelatieTypeMockMvc.perform(post("/api/relatie-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatieType)))
            .andExpect(status().isBadRequest());

        List<RelatieType> relatieTypeList = relatieTypeRepository.findAll();
        assertThat(relatieTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRelatieTypes() throws Exception {
        // Initialize the database
        relatieTypeRepository.saveAndFlush(relatieType);

        // Get all the relatieTypeList
        restRelatieTypeMockMvc.perform(get("/api/relatie-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatieType.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    @Test
    @Transactional
    public void getRelatieType() throws Exception {
        // Initialize the database
        relatieTypeRepository.saveAndFlush(relatieType);

        // Get the relatieType
        restRelatieTypeMockMvc.perform(get("/api/relatie-types/{id}", relatieType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(relatieType.getId().intValue()))
            .andExpect(jsonPath("$.omschrijving").value(DEFAULT_OMSCHRIJVING.toString()));
    }

    @Test
    @Transactional
    public void getAllRelatieTypesByOmschrijvingIsEqualToSomething() throws Exception {
        // Initialize the database
        relatieTypeRepository.saveAndFlush(relatieType);

        // Get all the relatieTypeList where omschrijving equals to DEFAULT_OMSCHRIJVING
        defaultRelatieTypeShouldBeFound("omschrijving.equals=" + DEFAULT_OMSCHRIJVING);

        // Get all the relatieTypeList where omschrijving equals to UPDATED_OMSCHRIJVING
        defaultRelatieTypeShouldNotBeFound("omschrijving.equals=" + UPDATED_OMSCHRIJVING);
    }

    @Test
    @Transactional
    public void getAllRelatieTypesByOmschrijvingIsInShouldWork() throws Exception {
        // Initialize the database
        relatieTypeRepository.saveAndFlush(relatieType);

        // Get all the relatieTypeList where omschrijving in DEFAULT_OMSCHRIJVING or UPDATED_OMSCHRIJVING
        defaultRelatieTypeShouldBeFound("omschrijving.in=" + DEFAULT_OMSCHRIJVING + "," + UPDATED_OMSCHRIJVING);

        // Get all the relatieTypeList where omschrijving equals to UPDATED_OMSCHRIJVING
        defaultRelatieTypeShouldNotBeFound("omschrijving.in=" + UPDATED_OMSCHRIJVING);
    }

    @Test
    @Transactional
    public void getAllRelatieTypesByOmschrijvingIsNullOrNotNull() throws Exception {
        // Initialize the database
        relatieTypeRepository.saveAndFlush(relatieType);

        // Get all the relatieTypeList where omschrijving is not null
        defaultRelatieTypeShouldBeFound("omschrijving.specified=true");

        // Get all the relatieTypeList where omschrijving is null
        defaultRelatieTypeShouldNotBeFound("omschrijving.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRelatieTypeShouldBeFound(String filter) throws Exception {
        restRelatieTypeMockMvc.perform(get("/api/relatie-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatieType.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRelatieTypeShouldNotBeFound(String filter) throws Exception {
        restRelatieTypeMockMvc.perform(get("/api/relatie-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingRelatieType() throws Exception {
        // Get the relatieType
        restRelatieTypeMockMvc.perform(get("/api/relatie-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRelatieType() throws Exception {
        // Initialize the database
        relatieTypeService.save(relatieType);

        int databaseSizeBeforeUpdate = relatieTypeRepository.findAll().size();

        // Update the relatieType
        RelatieType updatedRelatieType = relatieTypeRepository.findOne(relatieType.getId());
        // Disconnect from session so that the updates on updatedRelatieType are not directly saved in db
        em.detach(updatedRelatieType);
        updatedRelatieType
            .omschrijving(UPDATED_OMSCHRIJVING);

        restRelatieTypeMockMvc.perform(put("/api/relatie-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRelatieType)))
            .andExpect(status().isOk());

        // Validate the RelatieType in the database
        List<RelatieType> relatieTypeList = relatieTypeRepository.findAll();
        assertThat(relatieTypeList).hasSize(databaseSizeBeforeUpdate);
        RelatieType testRelatieType = relatieTypeList.get(relatieTypeList.size() - 1);
        assertThat(testRelatieType.getOmschrijving()).isEqualTo(UPDATED_OMSCHRIJVING);

        // Validate the RelatieType in Elasticsearch
        RelatieType relatieTypeEs = relatieTypeSearchRepository.findOne(testRelatieType.getId());
        assertThat(relatieTypeEs).isEqualToIgnoringGivenFields(testRelatieType);
    }

    @Test
    @Transactional
    public void updateNonExistingRelatieType() throws Exception {
        int databaseSizeBeforeUpdate = relatieTypeRepository.findAll().size();

        // Create the RelatieType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRelatieTypeMockMvc.perform(put("/api/relatie-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relatieType)))
            .andExpect(status().isCreated());

        // Validate the RelatieType in the database
        List<RelatieType> relatieTypeList = relatieTypeRepository.findAll();
        assertThat(relatieTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRelatieType() throws Exception {
        // Initialize the database
        relatieTypeService.save(relatieType);

        int databaseSizeBeforeDelete = relatieTypeRepository.findAll().size();

        // Get the relatieType
        restRelatieTypeMockMvc.perform(delete("/api/relatie-types/{id}", relatieType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean relatieTypeExistsInEs = relatieTypeSearchRepository.exists(relatieType.getId());
        assertThat(relatieTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<RelatieType> relatieTypeList = relatieTypeRepository.findAll();
        assertThat(relatieTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRelatieType() throws Exception {
        // Initialize the database
        relatieTypeService.save(relatieType);

        // Search the relatieType
        restRelatieTypeMockMvc.perform(get("/api/_search/relatie-types?query=id:" + relatieType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatieType.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RelatieType.class);
        RelatieType relatieType1 = new RelatieType();
        relatieType1.setId(1L);
        RelatieType relatieType2 = new RelatieType();
        relatieType2.setId(relatieType1.getId());
        assertThat(relatieType1).isEqualTo(relatieType2);
        relatieType2.setId(2L);
        assertThat(relatieType1).isNotEqualTo(relatieType2);
        relatieType1.setId(null);
        assertThat(relatieType1).isNotEqualTo(relatieType2);
    }
}
