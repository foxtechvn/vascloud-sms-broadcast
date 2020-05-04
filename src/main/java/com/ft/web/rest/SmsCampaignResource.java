package com.ft.web.rest;

import com.ft.service.SmsCampaignService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.service.dto.SmsCampaignDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ft.domain.SmsCampaign}.
 */
@RestController
@RequestMapping("/api")
public class SmsCampaignResource {

    private final Logger log = LoggerFactory.getLogger(SmsCampaignResource.class);

    private static final String ENTITY_NAME = "smsBroadcastSmsCampaign";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SmsCampaignService smsCampaignService;

    public SmsCampaignResource(SmsCampaignService smsCampaignService) {
        this.smsCampaignService = smsCampaignService;
    }

    /**
     * {@code POST  /sms-campaigns} : Create a new smsCampaign.
     *
     * @param smsCampaignDTO the smsCampaignDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smsCampaignDTO, or with status {@code 400 (Bad Request)} if the smsCampaign has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sms-campaigns")
    public ResponseEntity<SmsCampaignDTO> createSmsCampaign(@Valid @RequestBody SmsCampaignDTO smsCampaignDTO) throws URISyntaxException {
        log.debug("REST request to save SmsCampaign : {}", smsCampaignDTO);
        if (smsCampaignDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsCampaign cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsCampaignDTO result = smsCampaignService.save(smsCampaignDTO);
        return ResponseEntity.created(new URI("/api/sms-campaigns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-campaigns} : Updates an existing smsCampaign.
     *
     * @param smsCampaignDTO the smsCampaignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsCampaignDTO,
     * or with status {@code 400 (Bad Request)} if the smsCampaignDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsCampaignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-campaigns")
    public ResponseEntity<SmsCampaignDTO> updateSmsCampaign(@Valid @RequestBody SmsCampaignDTO smsCampaignDTO) throws URISyntaxException {
        log.debug("REST request to update SmsCampaign : {}", smsCampaignDTO);
        if (smsCampaignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsCampaignDTO result = smsCampaignService.save(smsCampaignDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsCampaignDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sms-campaigns} : get all the smsCampaigns.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of smsCampaigns in body.
     */
    @GetMapping("/sms-campaigns")
    public ResponseEntity<List<SmsCampaignDTO>> getAllSmsCampaigns(Pageable pageable) {
        log.debug("REST request to get a page of SmsCampaigns");
        Page<SmsCampaignDTO> page = smsCampaignService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sms-campaigns/:id} : get the "id" smsCampaign.
     *
     * @param id the id of the smsCampaignDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the smsCampaignDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sms-campaigns/{id}")
    public ResponseEntity<SmsCampaignDTO> getSmsCampaign(@PathVariable Long id) {
        log.debug("REST request to get SmsCampaign : {}", id);
        Optional<SmsCampaignDTO> smsCampaignDTO = smsCampaignService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsCampaignDTO);
    }

    /**
     * {@code DELETE  /sms-campaigns/:id} : delete the "id" smsCampaign.
     *
     * @param id the id of the smsCampaignDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sms-campaigns/{id}")
    public ResponseEntity<Void> deleteSmsCampaign(@PathVariable Long id) {
        log.debug("REST request to delete SmsCampaign : {}", id);
        smsCampaignService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
