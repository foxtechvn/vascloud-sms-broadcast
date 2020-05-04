package com.ft.service;

import com.ft.domain.CampaignSms;
import com.ft.repository.CampaignSmsRepository;
import com.ft.service.dto.CampaignSmsDTO;
import com.ft.service.mapper.CampaignSmsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CampaignSms}.
 */
@Service
@Transactional
public class CampaignSmsService {

    private final Logger log = LoggerFactory.getLogger(CampaignSmsService.class);

    private final CampaignSmsRepository campaignSmsRepository;

    private final CampaignSmsMapper campaignSmsMapper;

    public CampaignSmsService(CampaignSmsRepository campaignSmsRepository, CampaignSmsMapper campaignSmsMapper) {
        this.campaignSmsRepository = campaignSmsRepository;
        this.campaignSmsMapper = campaignSmsMapper;
    }

    /**
     * Save a campaignSms.
     *
     * @param campaignSmsDTO the entity to save.
     * @return the persisted entity.
     */
    public CampaignSmsDTO save(CampaignSmsDTO campaignSmsDTO) {
        log.debug("Request to save CampaignSms : {}", campaignSmsDTO);
        CampaignSms campaignSms = campaignSmsMapper.toEntity(campaignSmsDTO);
        campaignSms = campaignSmsRepository.save(campaignSms);
        return campaignSmsMapper.toDto(campaignSms);
    }

    /**
     * Get all the campaignSms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CampaignSmsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CampaignSms");
        return campaignSmsRepository.findAll(pageable)
            .map(campaignSmsMapper::toDto);
    }

    /**
     * Get one campaignSms by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CampaignSmsDTO> findOne(Long id) {
        log.debug("Request to get CampaignSms : {}", id);
        return campaignSmsRepository.findById(id)
            .map(campaignSmsMapper::toDto);
    }

    /**
     * Delete the campaignSms by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CampaignSms : {}", id);
        campaignSmsRepository.deleteById(id);
    }
}
