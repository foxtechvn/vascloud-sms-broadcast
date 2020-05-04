package com.ft.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ft.config.Constants;
import com.ft.domain.SmsCampaign;
import com.ft.repository.CampaignSmsRepository;
import com.ft.repository.SmsCampaignRepository;
import com.ft.vascloud.components.CampaignSmsSubmitCallable;
import com.hazelcast.core.HazelcastInstance;

@Service
public class CampaignProcessorService {

	private static final Logger log = LoggerFactory.getLogger(CampaignProcessorService.class);
	
	@Autowired
	HazelcastInstance hazelcastInstance;
	
	@Autowired
	SmsCampaignRepository cpRepo;
	
	@Autowired
	CampaignSmsRepository smsRepo;
	
	@Autowired
	CampaignImportService importService;
	/**
	 * Check for campaign activation, every 5 minute
	 */
	@Scheduled(fixedRate = 300000)
	public void updateCampaignState( ) {
		Instant now = Instant.now();
		Instant today = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
		
		importOneTimeCampaignForToday(today);
		
		importVariantCampaignForToday(today);
		
		activatePendingCampaigns(now);
		
		checkCampaignExpiry(now);
	}

	/**
	 * Stop all expired campaign
	 * @param now
	 */
	public int checkCampaignExpiry(Instant now) {
		log.debug("Stop expired campaign MOVED_PERMANENTLY --> EXPIRED");
		int result = 0;
		for (SmsCampaign cp : cpRepo.findAllByExpiredAtLessThanAndState(now, HttpStatus.MOVED_PERMANENTLY.value())) {
			cp.setState(HttpStatus.EXPECTATION_FAILED.value());
			log.debug("-- Campaign Expired: {}", cp.getName());
			cpRepo.save(cp);
			result ++;
		}
		return result;
	}

	/**
	 * Activate pending campaign
	 * @param now
	 */
	public int activatePendingCampaigns(Instant now) {
		log.debug("Starting to activate all pending campaign in state CHECKPOINT --> MOVED_PERMANENTLY");
		int result = 0;
		for (SmsCampaign cp : cpRepo.findAllByStartAtLessThanAndState(now, HttpStatus.CHECKPOINT.value())) {
			cp.setState(HttpStatus.MOVED_PERMANENTLY.value());
			log.debug("-- Campaign Active: {}", cp.getName());
			cpRepo.save(cp);
			result ++;
		}
		return result;
	}
	
	/**
	 * Archive campaign
	 */
//	@Scheduled(cron = "0 0 1 * * * ")
	public void archiveAllCampaign() {
		// Archive campaign that start a week ago
		for (SmsCampaign cp : 
			cpRepo.findAllByStartAtLessThanAndStateNotIn(
					Instant.now().minusSeconds(7 * 86400), 
					Arrays.asList( HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.BAD_GATEWAY.value() ))) {
			performArchive(cp);
		} 
	}

	@Async
	public void performArchive(SmsCampaign cp) {
		// TODO: Archive the campaign which no longer use
		cpRepo.save(cp);
	}

	/**
	 * Import Data for Variant Campaign
	 * @param today
	 */
	public int importVariantCampaignForToday(Instant today) {
		log.debug("Starting to import all pending campaign variant in state PROCESSING into pending");
		int result = 0;
		for (SmsCampaign cp : cpRepo.findAllByStartAtGreaterThanAndState(today, HttpStatus.OK.value())) {
			try {
				importService.importDataSource(cp);
				cp.setState(HttpStatus.CHECKPOINT.value());
				log.debug("-- Campaign Variant Imported: {}", cp.getName());
				cpRepo.save(cp);
			} catch (Exception e) {
				log.error("Cannot import data for campaign {}", cp, e);
				cp.setState(HttpStatus.BAD_REQUEST.value());
				cpRepo.save(cp);
			}
			result ++;
		}
		return result;
	}

	/**
	 * Import data for One time campaign
	 * @param today
	 */
	protected int importOneTimeCampaignForToday(Instant today) {
		log.debug("Starting to import all pending ONETIME campaign variant in state OK into pending");
		int result = 0;
		for (SmsCampaign cp : cpRepo.findAllByStartAtGreaterThanAndState(today, HttpStatus.OK.value())) {
			try {
				importService.importDataSource(cp);
				cp.setState(HttpStatus.CHECKPOINT.value());
				log.debug("-- Campaign ONE TIME imported: {}", cp.getName());
				cpRepo.save(cp);
			} catch (Exception e) {
				log.error("Cannot import data for campaign {}", cp, e);
				cp.setState(HttpStatus.BAD_REQUEST.value());
				cpRepo.save(cp);
			}
			result ++;
		}
		return result;
	}
	
	/**
	 * Set Campaign Status as need to enqueue for re-processing
	 * @param campaign
	 */
	public SmsCampaign startCampaign(SmsCampaign campaign) {
		campaign.setState(HttpStatus.PROCESSING.value());
		return cpRepo.save(campaign);
	}
	
	/**
	 * Temporary pause campaign
	 * @param campaign
	 */
	public SmsCampaign pauseCampaign(SmsCampaign campaign) {
		campaign.setState(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value());
		Future<Long> activeTask = CampaignSmsProcessorService.futures.get(campaign.getId());
		if ((activeTask != null) && !activeTask.isDone()) {
			activeTask.cancel(true);
		}
		return cpRepo.save(campaign);
	}
	
	/**
	 * Permanent stop the campaign, purge all pending SMS for this campaign
	 * @param campaign
	 */
	public SmsCampaign stopCampaign(SmsCampaign campaign) {
		campaign.setState(HttpStatus.PAYMENT_REQUIRED.value());
		smsRepo.deleteAllByCampaignAndState(campaign, HttpStatus.PROCESSING.value());
		Future<Long> activeTask = CampaignSmsProcessorService.futures.get(campaign.getId());
		if ((activeTask != null) && !activeTask.isDone()) {
			activeTask.cancel(true);
		}
		return cpRepo.save(campaign);
	}
	
	public SmsCampaign resumeCampaign(SmsCampaign campaign) {
		campaign.setState(HttpStatus.MOVED_PERMANENTLY.value());
		return cpRepo.save(campaign);
	}
	
	public void resetCampaignRateLimit(SmsCampaign campaign) {
		hazelcastInstance.getMap(Constants.MAP_TPS).remove(campaign.getId());
		CampaignSmsSubmitCallable activeTask = CampaignSmsProcessorService.activeTasks.get(campaign.getId());
		if (activeTask != null) {
			activeTask.setCampaign(campaign);
		}
		log.info("* New rate limit for campaign {} {} applied: {}", campaign.getId(), campaign.getName(), campaign.getRateLimit());
	}
	
	@Scheduled(cron = "0 0 12,21 * * *")
	public void dailyStats() {
		for (SmsCampaign cp : cpRepo.findAllByExpiredAtGreaterThan(Instant.now())) {
			campaignStatistics(cp);
		}
	}

	@Async
	public void campaignStatistics(SmsCampaign cp) {
		long total = smsRepo.countByCampaign(cp);
		long success = smsRepo.countByCampaignAndState(cp, HttpStatus.ACCEPTED.value());
		long failed = smsRepo.countByCampaignAndState(cp, HttpStatus.NOT_ACCEPTABLE.value());
		cpRepo.save(cp);
	}
}
