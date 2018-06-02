package eu.mayrns.romeo.kkf.web.rest;

import eu.mayrns.romeo.kkf.KkfApp;

import eu.mayrns.romeo.kkf.domain.AdresType;
import eu.mayrns.romeo.kkf.repository.AdresTypeRepository;
import eu.mayrns.romeo.kkf.service.AdresTypeService;
import eu.mayrns.romeo.kkf.repository.search.AdresTypeSearchRepository;
import eu.mayrns.romeo.kkf.web.rest.errors.ExceptionTranslator;
import eu.mayrns.romeo.kkf.service.dto.AdresTypeCriteria;
import eu.mayrns.romeo.kkf.service.AdresTypeQueryService;

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
 * Test class for the AdresTypeResource REST controller.
 *
 * @see AdresTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KkfApp.class)
public class AdresTypeResourceIntTest {

    private static final String DEFAULT_OMSCHRIJVING = "AAAAAAAAAA";
    private static final String UPDATED_OMSCHRIJVING = "BBBBBBBBBB";

    @Autowired
    private AdresTypeRepository adresTypeRepository;

    @Autowired
    private AdresTypeService adresTypeService;

    @Autowired
    private AdresTypeSearchRepository adresTypeSearchRepository;

    @Autowired
    private AdresTypeQueryService adresTypeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAdresTypeMockMvc;

    private AdresType adresType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdresTypeResource adresTypeResource = new AdresTypeResource(adresTypeService, adresTypeQueryService);
        this.restAdresTypeMockMvc = MockMvcBuilders.standaloneSetup(adresTypeResource)
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
    public static AdresType createEntity(EntityManager em) {
        AdresType adresType = new AdresType()
            .omschrijving(DEFAULT_OMSCHRIJVING);
        return adresType;
    }

    @Before
    public void initTest() {
        adresTypeSearchRepository.deleteAll();
        adresType = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdresType() throws Exception {
        int databaseSizeBeforeCreate = adresTypeRepository.findAll().size();

        // Create the AdresType
        restAdresTypeMockMvc.perform(post("/api/adres-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adresType)))
            .andExpect(status().isCreated());

        // Validate the AdresType in the database
        List<AdresType> adresTypeList = adresTypeRepository.findAll();
        assertThat(adresTypeList).hasSize(databaseSizeBeforeCreate + 1);
        AdresType testAdresType = adresTypeList.get(adresTypeList.size() - 1);
        assertThat(testAdresType.getOmschrijving()).isEqualTo(DEFAULT_OMSCHRIJVING);

        // Validate the AdresType in Elasticsearch
        AdresType adresTypeEs = adresTypeSearchRepository.findOne(testAdresType.getId());
        assertThat(adresTypeEs).isEqualToIgnoringGivenFields(testAdresType);
    }

    @Test
    @Transactional
    public void createAdresTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adresTypeRepository.findAll().size();

        // Create the AdresType with an existing ID
        adresType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdresTypeMockMvc.perform(post("/api/adres-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adresType)))
            .andExpect(status().isBadRequest());

        // Validate the AdresType in the database
        List<AdresType> adresTypeList = adresTypeRepository.findAll();
        assertThat(adresTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOmschrijvingIsRequired() throws Exception {
        int databaseSizeBeforeTest = adresTypeRepository.findAll().size();
        // set the field null
        adresType.setOmschrijving(null);

        // Create the AdresType, which fails.

        restAdresTypeMockMvc.perform(post("/api/adres-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adresType)))
            .andExpect(status().isBadRequest());

        List<AdresType> adresTypeList = adresTypeRepository.findAll();
        assertThat(adresTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdresTypes() throws Exception {
        // Initialize the database
        adresTypeRepository.saveAndFlush(adresType);

        // Get all the adresTypeList
        restAdresTypeMockMvc.perform(get("/api/adres-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adresType.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    @Test
    @Transactional
    public void getAdresType() throws Exception {
        // Initialize the database
        adresTypeRepository.saveAndFlush(adresType);

        // Get the adresType
        restAdresTypeMockMvc.perform(get("/api/adres-types/{id}", adresType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adresType.getId().intValue()))
            .andExpect(jsonPath("$.omschrijving").value(DEFAULT_OMSCHRIJVING.toString()));
    }

    @Test
    @Transactional
    public void getAllAdresTypesByOmschrijvingIsEqualToSomething() throws Exception {
        // Initialize the database
        adresTypeRepository.saveAndFlush(adresType);

        // Get all the adresTypeList where omschrijving equals to DEFAULT_OMSCHRIJVING
        defaultAdresTypeShouldBeFound("omschrijving.equals=" + DEFAULT_OMSCHRIJVING);

        // Get all the adresTypeList where omschrijving equals to UPDATED_OMSCHRIJVING
        defaultAdresTypeShouldNotBeFound("omschrijving.equals=" + UPDATED_OMSCHRIJVING);
    }

    @Test
    @Transactional
    public void getAllAdresTypesByOmschrijvingIsInShouldWork() throws Exception {
        // Initialize the database
        adresTypeRepository.saveAndFlush(adresType);

        // Get all the adresTypeList where omschrijving in DEFAULT_OMSCHRIJVING or UPDATED_OMSCHRIJVING
        defaultAdresTypeShouldBeFound("omschrijving.in=" + DEFAULT_OMSCHRIJVING + "," + UPDATED_OMSCHRIJVING);

        // Get all the adresTypeList where omschrijving equals to UPDATED_OMSCHRIJVING
        defaultAdresTypeShouldNotBeFound("omschrijving.in=" + UPDATED_OMSCHRIJVING);
    }

    @Test
    @Transactional
    public void getAllAdresTypesByOmschrijvingIsNullOrNotNull() throws Exception {
        // Initialize the database
        adresTypeRepository.saveAndFlush(adresType);

        // Get all the adresTypeList where omschrijving is not null
        defaultAdresTypeShouldBeFound("omschrijving.specified=true");

        // Get all the adresTypeList where omschrijving is null
        defaultAdresTypeShouldNotBeFound("omschrijving.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAdresTypeShouldBeFound(String filter) throws Exception {
        restAdresTypeMockMvc.perform(get("/api/adres-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adresType.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAdresTypeShouldNotBeFound(String filter) throws Exception {
        restAdresTypeMockMvc.perform(get("/api/adres-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAdresType() throws Exception {
        // Get the adresType
        restAdresTypeMockMvc.perform(get("/api/adres-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdresType() throws Exception {
        // Initialize the database
        adresTypeService.save(adresType);

        int databaseSizeBeforeUpdate = adresTypeRepository.findAll().size();

        // Update the adresType
        AdresType updatedAdresType = adresTypeRepository.findOne(adresType.getId());
        // Disconnect from session so that the updates on updatedAdresType are not directly saved in db
        em.detach(updatedAdresType);
        updatedAdresType
            .omschrijving(UPDATED_OMSCHRIJVING);

        restAdresTypeMockMvc.perform(put("/api/adres-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdresType)))
            .andExpect(status().isOk());

        // Validate the AdresType in the database
        List<AdresType> adresTypeList = adresTypeRepository.findAll();
        assertThat(adresTypeList).hasSize(databaseSizeBeforeUpdate);
        AdresType testAdresType = adresTypeList.get(adresTypeList.size() - 1);
        assertThat(testAdresType.getOmschrijving()).isEqualTo(UPDATED_OMSCHRIJVING);

        // Validate the AdresType in Elasticsearch
        AdresType adresTypeEs = adresTypeSearchRepository.findOne(testAdresType.getId());
        assertThat(adresTypeEs).isEqualToIgnoringGivenFields(testAdresType);
    }

    @Test
    @Transactional
    public void updateNonExistingAdresType() throws Exception {
        int databaseSizeBeforeUpdate = adresTypeRepository.findAll().size();

        // Create the AdresType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdresTypeMockMvc.perform(put("/api/adres-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adresType)))
            .andExpect(status().isCreated());

        // Validate the AdresType in the database
        List<AdresType> adresTypeList = adresTypeRepository.findAll();
        assertThat(adresTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAdresType() throws Exception {
        // Initialize the database
        adresTypeService.save(adresType);

        int databaseSizeBeforeDelete = adresTypeRepository.findAll().size();

        // Get the adresType
        restAdresTypeMockMvc.perform(delete("/api/adres-types/{id}", adresType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean adresTypeExistsInEs = adresTypeSearchRepository.exists(adresType.getId());
        assertThat(adresTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<AdresType> adresTypeList = adresTypeRepository.findAll();
        assertThat(adresTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAdresType() throws Exception {
        // Initialize the database
        adresTypeService.save(adresType);

        // Search the adresType
        restAdresTypeMockMvc.perform(get("/api/_search/adres-types?query=id:" + adresType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adresType.getId().intValue())))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdresType.class);
        AdresType adresType1 = new AdresType();
        adresType1.setId(1L);
        AdresType adresType2 = new AdresType();
        adresType2.setId(adresType1.getId());
        assertThat(adresType1).isEqualTo(adresType2);
        adresType2.setId(2L);
        assertThat(adresType1).isNotEqualTo(adresType2);
        adresType1.setId(null);
        assertThat(adresType1).isNotEqualTo(adresType2);
    }
}
