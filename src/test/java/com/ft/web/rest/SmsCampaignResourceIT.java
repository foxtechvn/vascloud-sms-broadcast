package com.ft.web.rest;

import com.ft.SmsBroadcastApp;
import com.ft.domain.SmsCampaign;
import com.ft.repository.SmsCampaignRepository;
import com.ft.service.SmsCampaignService;
import com.ft.service.dto.SmsCampaignDTO;
import com.ft.service.mapper.SmsCampaignMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SmsCampaignResource} REST controller.
 */
@SpringBootTest(classes = SmsBroadcastApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SmsCampaignResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final String DEFAULT_SHORT_MSG = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_MSG = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PDU = "AAAAAAAAAA";
    private static final String UPDATED_PDU = "BBBBBBBBBB";

    private static final String DEFAULT_VAS_CLOUD_PACKAGE = "AAAAAAAAAA";
    private static final String UPDATED_VAS_CLOUD_PACKAGE = "BBBBBBBBBB";

    private static final Long DEFAULT_RATE_LIMIT = 1L;
    private static final Long UPDATED_RATE_LIMIT = 2L;

    @Autowired
    private SmsCampaignRepository smsCampaignRepository;

    @Autowired
    private SmsCampaignMapper smsCampaignMapper;

    @Autowired
    private SmsCampaignService smsCampaignService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSmsCampaignMockMvc;

    private SmsCampaign smsCampaign;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsCampaign createEntity(EntityManager em) {
        SmsCampaign smsCampaign = new SmsCampaign()
            .name(DEFAULT_NAME)
            .state(DEFAULT_STATE)
            .shortMsg(DEFAULT_SHORT_MSG)
            .startAt(DEFAULT_START_AT)
            .expiredAt(DEFAULT_EXPIRED_AT)
            .pdu(DEFAULT_PDU)
            .vasCloudPackage(DEFAULT_VAS_CLOUD_PACKAGE)
            .rateLimit(DEFAULT_RATE_LIMIT);
        return smsCampaign;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsCampaign createUpdatedEntity(EntityManager em) {
        SmsCampaign smsCampaign = new SmsCampaign()
            .name(UPDATED_NAME)
            .state(UPDATED_STATE)
            .shortMsg(UPDATED_SHORT_MSG)
            .startAt(UPDATED_START_AT)
            .expiredAt(UPDATED_EXPIRED_AT)
            .pdu(UPDATED_PDU)
            .vasCloudPackage(UPDATED_VAS_CLOUD_PACKAGE)
            .rateLimit(UPDATED_RATE_LIMIT);
        return smsCampaign;
    }

    @BeforeEach
    public void initTest() {
        smsCampaign = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmsCampaign() throws Exception {
        int databaseSizeBeforeCreate = smsCampaignRepository.findAll().size();

        // Create the SmsCampaign
        SmsCampaignDTO smsCampaignDTO = smsCampaignMapper.toDto(smsCampaign);
        restSmsCampaignMockMvc.perform(post("/api/sms-campaigns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(smsCampaignDTO)))
            .andExpect(status().isCreated());

        // Validate the SmsCampaign in the database
        List<SmsCampaign> smsCampaignList = smsCampaignRepository.findAll();
        assertThat(smsCampaignList).hasSize(databaseSizeBeforeCreate + 1);
        SmsCampaign testSmsCampaign = smsCampaignList.get(smsCampaignList.size() - 1);
        assertThat(testSmsCampaign.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSmsCampaign.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSmsCampaign.getShortMsg()).isEqualTo(DEFAULT_SHORT_MSG);
        assertThat(testSmsCampaign.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testSmsCampaign.getExpiredAt()).isEqualTo(DEFAULT_EXPIRED_AT);
        assertThat(testSmsCampaign.getPdu()).isEqualTo(DEFAULT_PDU);
        assertThat(testSmsCampaign.getVasCloudPackage()).isEqualTo(DEFAULT_VAS_CLOUD_PACKAGE);
        assertThat(testSmsCampaign.getRateLimit()).isEqualTo(DEFAULT_RATE_LIMIT);
    }

    @Test
    @Transactional
    public void createSmsCampaignWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smsCampaignRepository.findAll().size();

        // Create the SmsCampaign with an existing ID
        smsCampaign.setId(1L);
        SmsCampaignDTO smsCampaignDTO = smsCampaignMapper.toDto(smsCampaign);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsCampaignMockMvc.perform(post("/api/sms-campaigns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(smsCampaignDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsCampaign in the database
        List<SmsCampaign> smsCampaignList = smsCampaignRepository.findAll();
        assertThat(smsCampaignList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsCampaignRepository.findAll().size();
        // set the field null
        smsCampaign.setName(null);

        // Create the SmsCampaign, which fails.
        SmsCampaignDTO smsCampaignDTO = smsCampaignMapper.toDto(smsCampaign);

        restSmsCampaignMockMvc.perform(post("/api/sms-campaigns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(smsCampaignDTO)))
            .andExpect(status().isBadRequest());

        List<SmsCampaign> smsCampaignList = smsCampaignRepository.findAll();
        assertThat(smsCampaignList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsCampaignRepository.findAll().size();
        // set the field null
        smsCampaign.setState(null);

        // Create the SmsCampaign, which fails.
        SmsCampaignDTO smsCampaignDTO = smsCampaignMapper.toDto(smsCampaign);

        restSmsCampaignMockMvc.perform(post("/api/sms-campaigns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(smsCampaignDTO)))
            .andExpect(status().isBadRequest());

        List<SmsCampaign> smsCampaignList = smsCampaignRepository.findAll();
        assertThat(smsCampaignList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSmsCampaigns() throws Exception {
        // Initialize the database
        smsCampaignRepository.saveAndFlush(smsCampaign);

        // Get all the smsCampaignList
        restSmsCampaignMockMvc.perform(get("/api/sms-campaigns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsCampaign.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].shortMsg").value(hasItem(DEFAULT_SHORT_MSG)))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].expiredAt").value(hasItem(DEFAULT_EXPIRED_AT.toString())))
            .andExpect(jsonPath("$.[*].pdu").value(hasItem(DEFAULT_PDU.toString())))
            .andExpect(jsonPath("$.[*].vasCloudPackage").value(hasItem(DEFAULT_VAS_CLOUD_PACKAGE)))
            .andExpect(jsonPath("$.[*].rateLimit").value(hasItem(DEFAULT_RATE_LIMIT.intValue())));
    }
    
    @Test
    @Transactional
    public void getSmsCampaign() throws Exception {
        // Initialize the database
        smsCampaignRepository.saveAndFlush(smsCampaign);

        // Get the smsCampaign
        restSmsCampaignMockMvc.perform(get("/api/sms-campaigns/{id}", smsCampaign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(smsCampaign.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.shortMsg").value(DEFAULT_SHORT_MSG))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT.toString()))
            .andExpect(jsonPath("$.expiredAt").value(DEFAULT_EXPIRED_AT.toString()))
            .andExpect(jsonPath("$.pdu").value(DEFAULT_PDU.toString()))
            .andExpect(jsonPath("$.vasCloudPackage").value(DEFAULT_VAS_CLOUD_PACKAGE))
            .andExpect(jsonPath("$.rateLimit").value(DEFAULT_RATE_LIMIT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSmsCampaign() throws Exception {
        // Get the smsCampaign
        restSmsCampaignMockMvc.perform(get("/api/sms-campaigns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmsCampaign() throws Exception {
        // Initialize the database
        smsCampaignRepository.saveAndFlush(smsCampaign);

        int databaseSizeBeforeUpdate = smsCampaignRepository.findAll().size();

        // Update the smsCampaign
        SmsCampaign updatedSmsCampaign = smsCampaignRepository.findById(smsCampaign.getId()).get();
        // Disconnect from session so that the updates on updatedSmsCampaign are not directly saved in db
        em.detach(updatedSmsCampaign);
        updatedSmsCampaign
            .name(UPDATED_NAME)
            .state(UPDATED_STATE)
            .shortMsg(UPDATED_SHORT_MSG)
            .startAt(UPDATED_START_AT)
            .expiredAt(UPDATED_EXPIRED_AT)
            .pdu(UPDATED_PDU)
            .vasCloudPackage(UPDATED_VAS_CLOUD_PACKAGE)
            .rateLimit(UPDATED_RATE_LIMIT);
        SmsCampaignDTO smsCampaignDTO = smsCampaignMapper.toDto(updatedSmsCampaign);

        restSmsCampaignMockMvc.perform(put("/api/sms-campaigns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(smsCampaignDTO)))
            .andExpect(status().isOk());

        // Validate the SmsCampaign in the database
        List<SmsCampaign> smsCampaignList = smsCampaignRepository.findAll();
        assertThat(smsCampaignList).hasSize(databaseSizeBeforeUpdate);
        SmsCampaign testSmsCampaign = smsCampaignList.get(smsCampaignList.size() - 1);
        assertThat(testSmsCampaign.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSmsCampaign.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSmsCampaign.getShortMsg()).isEqualTo(UPDATED_SHORT_MSG);
        assertThat(testSmsCampaign.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testSmsCampaign.getExpiredAt()).isEqualTo(UPDATED_EXPIRED_AT);
        assertThat(testSmsCampaign.getPdu()).isEqualTo(UPDATED_PDU);
        assertThat(testSmsCampaign.getVasCloudPackage()).isEqualTo(UPDATED_VAS_CLOUD_PACKAGE);
        assertThat(testSmsCampaign.getRateLimit()).isEqualTo(UPDATED_RATE_LIMIT);
    }

    @Test
    @Transactional
    public void updateNonExistingSmsCampaign() throws Exception {
        int databaseSizeBeforeUpdate = smsCampaignRepository.findAll().size();

        // Create the SmsCampaign
        SmsCampaignDTO smsCampaignDTO = smsCampaignMapper.toDto(smsCampaign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsCampaignMockMvc.perform(put("/api/sms-campaigns")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(smsCampaignDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsCampaign in the database
        List<SmsCampaign> smsCampaignList = smsCampaignRepository.findAll();
        assertThat(smsCampaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmsCampaign() throws Exception {
        // Initialize the database
        smsCampaignRepository.saveAndFlush(smsCampaign);

        int databaseSizeBeforeDelete = smsCampaignRepository.findAll().size();

        // Delete the smsCampaign
        restSmsCampaignMockMvc.perform(delete("/api/sms-campaigns/{id}", smsCampaign.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SmsCampaign> smsCampaignList = smsCampaignRepository.findAll();
        assertThat(smsCampaignList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
