package eu.mayrns.romeo.kkf.web.rest;

import eu.mayrns.romeo.kkf.KkfApp;

import eu.mayrns.romeo.kkf.domain.Adres;
import eu.mayrns.romeo.kkf.domain.AdresType;
import eu.mayrns.romeo.kkf.repository.AdresRepository;
import eu.mayrns.romeo.kkf.service.AdresService;
import eu.mayrns.romeo.kkf.repository.search.AdresSearchRepository;
import eu.mayrns.romeo.kkf.web.rest.errors.ExceptionTranslator;
import eu.mayrns.romeo.kkf.service.dto.AdresCriteria;
import eu.mayrns.romeo.kkf.service.AdresQueryService;

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
 * Test class for the AdresResource REST controller.
 *
 * @see AdresResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KkfApp.class)
public class AdresResourceIntTest {

    private static final String DEFAULT_STRAATNAAM = "AAAAAAAAAA";
    private static final String UPDATED_STRAATNAAM = "BBBBBBBBBB";

    private static final String DEFAULT_HUISNUMMER = "AAAAAAAAAA";
    private static final String UPDATED_HUISNUMMER = "BBBBBBBBBB";

    private static final String DEFAULT_POSTCODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTCODE = "BBBBBBBBBB";

    private static final String DEFAULT_STAD = "AAAAAAAAAA";
    private static final String UPDATED_STAD = "BBBBBBBBBB";

    @Autowired
    private AdresRepository adresRepository;

    @Autowired
    private AdresService adresService;

    @Autowired
    private AdresSearchRepository adresSearchRepository;

    @Autowired
    private AdresQueryService adresQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAdresMockMvc;

    private Adres adres;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdresResource adresResource = new AdresResource(adresService, adresQueryService);
        this.restAdresMockMvc = MockMvcBuilders.standaloneSetup(adresResource)
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
    public static Adres createEntity(EntityManager em) {
        Adres adres = new Adres()
            .straatnaam(DEFAULT_STRAATNAAM)
            .huisnummer(DEFAULT_HUISNUMMER)
            .postcode(DEFAULT_POSTCODE)
            .stad(DEFAULT_STAD);
        return adres;
    }

    @Before
    public void initTest() {
        adresSearchRepository.deleteAll();
        adres = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdres() throws Exception {
        int databaseSizeBeforeCreate = adresRepository.findAll().size();

        // Create the Adres
        restAdresMockMvc.perform(post("/api/adres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adres)))
            .andExpect(status().isCreated());

        // Validate the Adres in the database
        List<Adres> adresList = adresRepository.findAll();
        assertThat(adresList).hasSize(databaseSizeBeforeCreate + 1);
        Adres testAdres = adresList.get(adresList.size() - 1);
        assertThat(testAdres.getStraatnaam()).isEqualTo(DEFAULT_STRAATNAAM);
        assertThat(testAdres.getHuisnummer()).isEqualTo(DEFAULT_HUISNUMMER);
        assertThat(testAdres.getPostcode()).isEqualTo(DEFAULT_POSTCODE);
        assertThat(testAdres.getStad()).isEqualTo(DEFAULT_STAD);

        // Validate the Adres in Elasticsearch
        Adres adresEs = adresSearchRepository.findOne(testAdres.getId());
        assertThat(adresEs).isEqualToIgnoringGivenFields(testAdres);
    }

    @Test
    @Transactional
    public void createAdresWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adresRepository.findAll().size();

        // Create the Adres with an existing ID
        adres.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdresMockMvc.perform(post("/api/adres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adres)))
            .andExpect(status().isBadRequest());

        // Validate the Adres in the database
        List<Adres> adresList = adresRepository.findAll();
        assertThat(adresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStraatnaamIsRequired() throws Exception {
        int databaseSizeBeforeTest = adresRepository.findAll().size();
        // set the field null
        adres.setStraatnaam(null);

        // Create the Adres, which fails.

        restAdresMockMvc.perform(post("/api/adres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adres)))
            .andExpect(status().isBadRequest());

        List<Adres> adresList = adresRepository.findAll();
        assertThat(adresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdres() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList
        restAdresMockMvc.perform(get("/api/adres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adres.getId().intValue())))
            .andExpect(jsonPath("$.[*].straatnaam").value(hasItem(DEFAULT_STRAATNAAM.toString())))
            .andExpect(jsonPath("$.[*].huisnummer").value(hasItem(DEFAULT_HUISNUMMER.toString())))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE.toString())))
            .andExpect(jsonPath("$.[*].stad").value(hasItem(DEFAULT_STAD.toString())));
    }

    @Test
    @Transactional
    public void getAdres() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get the adres
        restAdresMockMvc.perform(get("/api/adres/{id}", adres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adres.getId().intValue()))
            .andExpect(jsonPath("$.straatnaam").value(DEFAULT_STRAATNAAM.toString()))
            .andExpect(jsonPath("$.huisnummer").value(DEFAULT_HUISNUMMER.toString()))
            .andExpect(jsonPath("$.postcode").value(DEFAULT_POSTCODE.toString()))
            .andExpect(jsonPath("$.stad").value(DEFAULT_STAD.toString()));
    }

    @Test
    @Transactional
    public void getAllAdresByStraatnaamIsEqualToSomething() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where straatnaam equals to DEFAULT_STRAATNAAM
        defaultAdresShouldBeFound("straatnaam.equals=" + DEFAULT_STRAATNAAM);

        // Get all the adresList where straatnaam equals to UPDATED_STRAATNAAM
        defaultAdresShouldNotBeFound("straatnaam.equals=" + UPDATED_STRAATNAAM);
    }

    @Test
    @Transactional
    public void getAllAdresByStraatnaamIsInShouldWork() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where straatnaam in DEFAULT_STRAATNAAM or UPDATED_STRAATNAAM
        defaultAdresShouldBeFound("straatnaam.in=" + DEFAULT_STRAATNAAM + "," + UPDATED_STRAATNAAM);

        // Get all the adresList where straatnaam equals to UPDATED_STRAATNAAM
        defaultAdresShouldNotBeFound("straatnaam.in=" + UPDATED_STRAATNAAM);
    }

    @Test
    @Transactional
    public void getAllAdresByStraatnaamIsNullOrNotNull() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where straatnaam is not null
        defaultAdresShouldBeFound("straatnaam.specified=true");

        // Get all the adresList where straatnaam is null
        defaultAdresShouldNotBeFound("straatnaam.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdresByHuisnummerIsEqualToSomething() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where huisnummer equals to DEFAULT_HUISNUMMER
        defaultAdresShouldBeFound("huisnummer.equals=" + DEFAULT_HUISNUMMER);

        // Get all the adresList where huisnummer equals to UPDATED_HUISNUMMER
        defaultAdresShouldNotBeFound("huisnummer.equals=" + UPDATED_HUISNUMMER);
    }

    @Test
    @Transactional
    public void getAllAdresByHuisnummerIsInShouldWork() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where huisnummer in DEFAULT_HUISNUMMER or UPDATED_HUISNUMMER
        defaultAdresShouldBeFound("huisnummer.in=" + DEFAULT_HUISNUMMER + "," + UPDATED_HUISNUMMER);

        // Get all the adresList where huisnummer equals to UPDATED_HUISNUMMER
        defaultAdresShouldNotBeFound("huisnummer.in=" + UPDATED_HUISNUMMER);
    }

    @Test
    @Transactional
    public void getAllAdresByHuisnummerIsNullOrNotNull() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where huisnummer is not null
        defaultAdresShouldBeFound("huisnummer.specified=true");

        // Get all the adresList where huisnummer is null
        defaultAdresShouldNotBeFound("huisnummer.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdresByPostcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where postcode equals to DEFAULT_POSTCODE
        defaultAdresShouldBeFound("postcode.equals=" + DEFAULT_POSTCODE);

        // Get all the adresList where postcode equals to UPDATED_POSTCODE
        defaultAdresShouldNotBeFound("postcode.equals=" + UPDATED_POSTCODE);
    }

    @Test
    @Transactional
    public void getAllAdresByPostcodeIsInShouldWork() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where postcode in DEFAULT_POSTCODE or UPDATED_POSTCODE
        defaultAdresShouldBeFound("postcode.in=" + DEFAULT_POSTCODE + "," + UPDATED_POSTCODE);

        // Get all the adresList where postcode equals to UPDATED_POSTCODE
        defaultAdresShouldNotBeFound("postcode.in=" + UPDATED_POSTCODE);
    }

    @Test
    @Transactional
    public void getAllAdresByPostcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where postcode is not null
        defaultAdresShouldBeFound("postcode.specified=true");

        // Get all the adresList where postcode is null
        defaultAdresShouldNotBeFound("postcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdresByStadIsEqualToSomething() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where stad equals to DEFAULT_STAD
        defaultAdresShouldBeFound("stad.equals=" + DEFAULT_STAD);

        // Get all the adresList where stad equals to UPDATED_STAD
        defaultAdresShouldNotBeFound("stad.equals=" + UPDATED_STAD);
    }

    @Test
    @Transactional
    public void getAllAdresByStadIsInShouldWork() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where stad in DEFAULT_STAD or UPDATED_STAD
        defaultAdresShouldBeFound("stad.in=" + DEFAULT_STAD + "," + UPDATED_STAD);

        // Get all the adresList where stad equals to UPDATED_STAD
        defaultAdresShouldNotBeFound("stad.in=" + UPDATED_STAD);
    }

    @Test
    @Transactional
    public void getAllAdresByStadIsNullOrNotNull() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList where stad is not null
        defaultAdresShouldBeFound("stad.specified=true");

        // Get all the adresList where stad is null
        defaultAdresShouldNotBeFound("stad.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdresByAdresTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        AdresType adresType = AdresTypeResourceIntTest.createEntity(em);
        em.persist(adresType);
        em.flush();
        adres.setAdresType(adresType);
        adresRepository.saveAndFlush(adres);
        Long adresTypeId = adresType.getId();

        // Get all the adresList where adresType equals to adresTypeId
        defaultAdresShouldBeFound("adresTypeId.equals=" + adresTypeId);

        // Get all the adresList where adresType equals to adresTypeId + 1
        defaultAdresShouldNotBeFound("adresTypeId.equals=" + (adresTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAdresShouldBeFound(String filter) throws Exception {
        restAdresMockMvc.perform(get("/api/adres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adres.getId().intValue())))
            .andExpect(jsonPath("$.[*].straatnaam").value(hasItem(DEFAULT_STRAATNAAM.toString())))
            .andExpect(jsonPath("$.[*].huisnummer").value(hasItem(DEFAULT_HUISNUMMER.toString())))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE.toString())))
            .andExpect(jsonPath("$.[*].stad").value(hasItem(DEFAULT_STAD.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAdresShouldNotBeFound(String filter) throws Exception {
        restAdresMockMvc.perform(get("/api/adres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAdres() throws Exception {
        // Get the adres
        restAdresMockMvc.perform(get("/api/adres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdres() throws Exception {
        // Initialize the database
        adresService.save(adres);

        int databaseSizeBeforeUpdate = adresRepository.findAll().size();

        // Update the adres
        Adres updatedAdres = adresRepository.findOne(adres.getId());
        // Disconnect from session so that the updates on updatedAdres are not directly saved in db
        em.detach(updatedAdres);
        updatedAdres
            .straatnaam(UPDATED_STRAATNAAM)
            .huisnummer(UPDATED_HUISNUMMER)
            .postcode(UPDATED_POSTCODE)
            .stad(UPDATED_STAD);

        restAdresMockMvc.perform(put("/api/adres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdres)))
            .andExpect(status().isOk());

        // Validate the Adres in the database
        List<Adres> adresList = adresRepository.findAll();
        assertThat(adresList).hasSize(databaseSizeBeforeUpdate);
        Adres testAdres = adresList.get(adresList.size() - 1);
        assertThat(testAdres.getStraatnaam()).isEqualTo(UPDATED_STRAATNAAM);
        assertThat(testAdres.getHuisnummer()).isEqualTo(UPDATED_HUISNUMMER);
        assertThat(testAdres.getPostcode()).isEqualTo(UPDATED_POSTCODE);
        assertThat(testAdres.getStad()).isEqualTo(UPDATED_STAD);

        // Validate the Adres in Elasticsearch
        Adres adresEs = adresSearchRepository.findOne(testAdres.getId());
        assertThat(adresEs).isEqualToIgnoringGivenFields(testAdres);
    }

    @Test
    @Transactional
    public void updateNonExistingAdres() throws Exception {
        int databaseSizeBeforeUpdate = adresRepository.findAll().size();

        // Create the Adres

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdresMockMvc.perform(put("/api/adres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adres)))
            .andExpect(status().isCreated());

        // Validate the Adres in the database
        List<Adres> adresList = adresRepository.findAll();
        assertThat(adresList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAdres() throws Exception {
        // Initialize the database
        adresService.save(adres);

        int databaseSizeBeforeDelete = adresRepository.findAll().size();

        // Get the adres
        restAdresMockMvc.perform(delete("/api/adres/{id}", adres.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean adresExistsInEs = adresSearchRepository.exists(adres.getId());
        assertThat(adresExistsInEs).isFalse();

        // Validate the database is empty
        List<Adres> adresList = adresRepository.findAll();
        assertThat(adresList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAdres() throws Exception {
        // Initialize the database
        adresService.save(adres);

        // Search the adres
        restAdresMockMvc.perform(get("/api/_search/adres?query=id:" + adres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adres.getId().intValue())))
            .andExpect(jsonPath("$.[*].straatnaam").value(hasItem(DEFAULT_STRAATNAAM.toString())))
            .andExpect(jsonPath("$.[*].huisnummer").value(hasItem(DEFAULT_HUISNUMMER.toString())))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE.toString())))
            .andExpect(jsonPath("$.[*].stad").value(hasItem(DEFAULT_STAD.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adres.class);
        Adres adres1 = new Adres();
        adres1.setId(1L);
        Adres adres2 = new Adres();
        adres2.setId(adres1.getId());
        assertThat(adres1).isEqualTo(adres2);
        adres2.setId(2L);
        assertThat(adres1).isNotEqualTo(adres2);
        adres1.setId(null);
        assertThat(adres1).isNotEqualTo(adres2);
    }
}
