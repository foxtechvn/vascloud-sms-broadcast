package com.ft.service;

import com.ft.domain.SmsCampaign;
import com.ft.repository.SmsCampaignRepository;
import com.ft.service.dto.SmsCampaignDTO;
import com.ft.service.mapper.SmsCampaignMapper;
import com.querydsl.core.types.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SmsCampaign}.
 */
@Service
@Transactional
public class SmsCampaignService {

    private final Logger log = LoggerFactory.getLogger(SmsCampaignService.class);

    private final SmsCampaignRepository smsCampaignRepository;

    private final SmsCampaignMapper smsCampaignMapper;

    public SmsCampaignService(SmsCampaignRepository smsSmsCampaignRepository, SmsCampaignMapper smsCampaignMapper) {
        this.smsCampaignRepository = smsSmsCampaignRepository;
        this.smsCampaignMapper = smsCampaignMapper;
    }

    /**
     * Save a smsCampaign.
     *
     * @param smsCampaignDTO the entity to save.
     * @return the persisted entity.
     */
    public SmsCampaignDTO save(SmsCampaignDTO smsCampaignDTO) {
        log.debug("Request to save SmsCampaign : {}", smsCampaignDTO);
        SmsCampaign smsCampaign = smsCampaignMapper.toEntity(smsCampaignDTO);
        smsCampaign = smsCampaignRepository.save(smsCampaign);
        return smsCampaignMapper.toDto(smsCampaign);
    }

    /**
     * Get all the smsCampaigns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SmsCampaignDTO> findAll(Predicate predicate, Pageable pageable) {
        log.debug("Request to get all SmsCampaigns");
        return (predicate == null ? smsCampaignRepository.findAll(pageable) : smsCampaignRepository.findAll(predicate, pageable))
            .map(smsCampaignMapper::toDto);
    }

    /**
     * Get one smsCampaign by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SmsCampaignDTO> findOne(Long id) {
        log.debug("Request to get SmsCampaign : {}", id);
        return smsCampaignRepository.findById(id)
            .map(smsCampaignMapper::toDto);
    }

    /**
     * Delete the smsCampaign by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SmsCampaign : {}", id);
        smsCampaignRepository.deleteById(id);
    }
}
