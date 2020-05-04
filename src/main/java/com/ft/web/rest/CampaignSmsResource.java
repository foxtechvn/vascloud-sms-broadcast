package com.ft.web.rest;

import com.ft.service.CampaignSmsService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.querydsl.core.types.Predicate;
import com.ft.service.dto.CampaignSmsDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ft.domain.CampaignSms}.
 */
@RestController
@RequestMapping("/api")
public class CampaignSmsResource {

    private final Logger log = LoggerFactory.getLogger(CampaignSmsResource.class);

    private static final String ENTITY_NAME = "smsBroadcastCampaignSms";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CampaignSmsService campaignSmsService;

    public CampaignSmsResource(CampaignSmsService campaignSmsService) {
        this.campaignSmsService = campaignSmsService;
    }

    /**
     * {@code POST  /campaign-sms} : Create a new campaignSms.
     *
     * @param campaignSmsDTO the campaignSmsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new campaignSmsDTO, or with status {@code 400 (Bad Request)} if the campaignSms has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/campaign-sms")
    public ResponseEntity<CampaignSmsDTO> createCampaignSms(@RequestBody CampaignSmsDTO campaignSmsDTO) throws URISyntaxException {
        log.debug("REST request to save CampaignSms : {}", campaignSmsDTO);
        if (campaignSmsDTO.getId() != null) {
            throw new BadRequestAlertException("A new campaignSms cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CampaignSmsDTO result = campaignSmsService.save(campaignSmsDTO);
        return ResponseEntity.created(new URI("/api/campaign-sms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /campaign-sms} : Updates an existing campaignSms.
     *
     * @param campaignSmsDTO the campaignSmsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campaignSmsDTO,
     * or with status {@code 400 (Bad Request)} if the campaignSmsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the campaignSmsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/campaign-sms")
    public ResponseEntity<CampaignSmsDTO> updateCampaignSms(@RequestBody CampaignSmsDTO campaignSmsDTO) throws URISyntaxException {
        log.debug("REST request to update CampaignSms : {}", campaignSmsDTO);
        if (campaignSmsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CampaignSmsDTO result = campaignSmsService.save(campaignSmsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, campaignSmsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /campaign-sms} : get all the campaignSms.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of campaignSms in body.
     */
    @GetMapping("/campaign-sms")
    public ResponseEntity<List<CampaignSmsDTO>> getAllCampaignSms(Predicate predicate, Pageable pageable) {
        log.debug("REST request to get a page of CampaignSms");
        Page<CampaignSmsDTO> page = campaignSmsService.findAll(predicate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /campaign-sms/:id} : get the "id" campaignSms.
     *
     * @param id the id of the campaignSmsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the campaignSmsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/campaign-sms/{id}")
    public ResponseEntity<CampaignSmsDTO> getCampaignSms(@PathVariable Long id) {
        log.debug("REST request to get CampaignSms : {}", id);
        Optional<CampaignSmsDTO> campaignSmsDTO = campaignSmsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(campaignSmsDTO);
    }

    /**
     * {@code DELETE  /campaign-sms/:id} : delete the "id" campaignSms.
     *
     * @param id the id of the campaignSmsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/campaign-sms/{id}")
    public ResponseEntity<Void> deleteCampaignSms(@PathVariable Long id) {
        log.debug("REST request to delete CampaignSms : {}", id);
        campaignSmsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
