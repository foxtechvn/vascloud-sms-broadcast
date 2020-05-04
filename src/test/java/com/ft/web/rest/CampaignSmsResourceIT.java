package com.ft.web.rest;

import com.ft.SmsBroadcastApp;
import com.ft.domain.CampaignSms;
import com.ft.repository.CampaignSmsRepository;
import com.ft.service.CampaignSmsService;
import com.ft.service.dto.CampaignSmsDTO;
import com.ft.service.mapper.CampaignSmsMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CampaignSmsResource} REST controller.
 */
@SpringBootTest(classes = SmsBroadcastApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class CampaignSmsResourceIT {

    private static final String DEFAULT_MSISDN = "AAAAAAAAAA";
    private static final String UPDATED_MSISDN = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final Instant DEFAULT_SUBMIT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMIT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REQUEST_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUEST_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CampaignSmsRepository campaignSmsRepository;

    @Autowired
    private CampaignSmsMapper campaignSmsMapper;

    @Autowired
    private CampaignSmsService campaignSmsService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCampaignSmsMockMvc;

    private CampaignSms campaignSms;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CampaignSms createEntity(EntityManager em) {
        CampaignSms campaignSms = new CampaignSms()
            .msisdn(DEFAULT_MSISDN)
            .text(DEFAULT_TEXT)
            .state(DEFAULT_STATE)
            .submitAt(DEFAULT_SUBMIT_AT)
            .expiredAt(DEFAULT_EXPIRED_AT)
            .requestAt(DEFAULT_REQUEST_AT);
        return campaignSms;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CampaignSms createUpdatedEntity(EntityManager em) {
        CampaignSms campaignSms = new CampaignSms()
            .msisdn(UPDATED_MSISDN)
            .text(UPDATED_TEXT)
            .state(UPDATED_STATE)
            .submitAt(UPDATED_SUBMIT_AT)
            .expiredAt(UPDATED_EXPIRED_AT)
            .requestAt(UPDATED_REQUEST_AT);
        return campaignSms;
    }

    @BeforeEach
    public void initTest() {
        campaignSms = createEntity(em);
    }

    @Test
    @Transactional
    public void createCampaignSms() throws Exception {
        int databaseSizeBeforeCreate = campaignSmsRepository.findAll().size();

        // Create the CampaignSms
        CampaignSmsDTO campaignSmsDTO = campaignSmsMapper.toDto(campaignSms);
        restCampaignSmsMockMvc.perform(post("/api/campaign-sms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(campaignSmsDTO)))
            .andExpect(status().isCreated());

        // Validate the CampaignSms in the database
        List<CampaignSms> campaignSmsList = campaignSmsRepository.findAll();
        assertThat(campaignSmsList).hasSize(databaseSizeBeforeCreate + 1);
        CampaignSms testCampaignSms = campaignSmsList.get(campaignSmsList.size() - 1);
        assertThat(testCampaignSms.getMsisdn()).isEqualTo(DEFAULT_MSISDN);
        assertThat(testCampaignSms.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testCampaignSms.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testCampaignSms.getSubmitAt()).isEqualTo(DEFAULT_SUBMIT_AT);
        assertThat(testCampaignSms.getExpiredAt()).isEqualTo(DEFAULT_EXPIRED_AT);
        assertThat(testCampaignSms.getRequestAt()).isEqualTo(DEFAULT_REQUEST_AT);
    }

    @Test
    @Transactional
    public void createCampaignSmsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = campaignSmsRepository.findAll().size();

        // Create the CampaignSms with an existing ID
        campaignSms.setId(1L);
        CampaignSmsDTO campaignSmsDTO = campaignSmsMapper.toDto(campaignSms);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampaignSmsMockMvc.perform(post("/api/campaign-sms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(campaignSmsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CampaignSms in the database
        List<CampaignSms> campaignSmsList = campaignSmsRepository.findAll();
        assertThat(campaignSmsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCampaignSms() throws Exception {
        // Initialize the database
        campaignSmsRepository.saveAndFlush(campaignSms);

        // Get all the campaignSmsList
        restCampaignSmsMockMvc.perform(get("/api/campaign-sms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campaignSms.getId().intValue())))
            .andExpect(jsonPath("$.[*].msisdn").value(hasItem(DEFAULT_MSISDN)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].submitAt").value(hasItem(DEFAULT_SUBMIT_AT.toString())))
            .andExpect(jsonPath("$.[*].expiredAt").value(hasItem(DEFAULT_EXPIRED_AT.toString())))
            .andExpect(jsonPath("$.[*].requestAt").value(hasItem(DEFAULT_REQUEST_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getCampaignSms() throws Exception {
        // Initialize the database
        campaignSmsRepository.saveAndFlush(campaignSms);

        // Get the campaignSms
        restCampaignSmsMockMvc.perform(get("/api/campaign-sms/{id}", campaignSms.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(campaignSms.getId().intValue()))
            .andExpect(jsonPath("$.msisdn").value(DEFAULT_MSISDN))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.submitAt").value(DEFAULT_SUBMIT_AT.toString()))
            .andExpect(jsonPath("$.expiredAt").value(DEFAULT_EXPIRED_AT.toString()))
            .andExpect(jsonPath("$.requestAt").value(DEFAULT_REQUEST_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCampaignSms() throws Exception {
        // Get the campaignSms
        restCampaignSmsMockMvc.perform(get("/api/campaign-sms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCampaignSms() throws Exception {
        // Initialize the database
        campaignSmsRepository.saveAndFlush(campaignSms);

        int databaseSizeBeforeUpdate = campaignSmsRepository.findAll().size();

        // Update the campaignSms
        CampaignSms updatedCampaignSms = campaignSmsRepository.findById(campaignSms.getId()).get();
        // Disconnect from session so that the updates on updatedCampaignSms are not directly saved in db
        em.detach(updatedCampaignSms);
        updatedCampaignSms
            .msisdn(UPDATED_MSISDN)
            .text(UPDATED_TEXT)
            .state(UPDATED_STATE)
            .submitAt(UPDATED_SUBMIT_AT)
            .expiredAt(UPDATED_EXPIRED_AT)
            .requestAt(UPDATED_REQUEST_AT);
        CampaignSmsDTO campaignSmsDTO = campaignSmsMapper.toDto(updatedCampaignSms);

        restCampaignSmsMockMvc.perform(put("/api/campaign-sms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(campaignSmsDTO)))
            .andExpect(status().isOk());

        // Validate the CampaignSms in the database
        List<CampaignSms> campaignSmsList = campaignSmsRepository.findAll();
        assertThat(campaignSmsList).hasSize(databaseSizeBeforeUpdate);
        CampaignSms testCampaignSms = campaignSmsList.get(campaignSmsList.size() - 1);
        assertThat(testCampaignSms.getMsisdn()).isEqualTo(UPDATED_MSISDN);
        assertThat(testCampaignSms.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testCampaignSms.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCampaignSms.getSubmitAt()).isEqualTo(UPDATED_SUBMIT_AT);
        assertThat(testCampaignSms.getExpiredAt()).isEqualTo(UPDATED_EXPIRED_AT);
        assertThat(testCampaignSms.getRequestAt()).isEqualTo(UPDATED_REQUEST_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingCampaignSms() throws Exception {
        int databaseSizeBeforeUpdate = campaignSmsRepository.findAll().size();

        // Create the CampaignSms
        CampaignSmsDTO campaignSmsDTO = campaignSmsMapper.toDto(campaignSms);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampaignSmsMockMvc.perform(put("/api/campaign-sms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(campaignSmsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CampaignSms in the database
        List<CampaignSms> campaignSmsList = campaignSmsRepository.findAll();
        assertThat(campaignSmsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCampaignSms() throws Exception {
        // Initialize the database
        campaignSmsRepository.saveAndFlush(campaignSms);

        int databaseSizeBeforeDelete = campaignSmsRepository.findAll().size();

        // Delete the campaignSms
        restCampaignSmsMockMvc.perform(delete("/api/campaign-sms/{id}", campaignSms.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CampaignSms> campaignSmsList = campaignSmsRepository.findAll();
        assertThat(campaignSmsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
