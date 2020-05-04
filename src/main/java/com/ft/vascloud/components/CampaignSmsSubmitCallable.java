package com.ft.vascloud.components;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.config.RateLimitConfiguration;
import com.ft.domain.SmsCampaign;
import com.ft.domain.CampaignSms;
import com.ft.repository.SmsCampaignRepository;
import com.ft.repository.CampaignSmsRepository;
import com.ft.service.CampaignSmsProcessorService;
import com.ft.service.VascloudEventService;
import com.ft.vascloud.service.MessageUtils;
import com.ft.vascloud.service.dto.VasCloudSmsRequestDTO;
import com.ft.vascloud.service.dto.VasCloudSmsResponseDTO;
import com.ft.vascloud.service.dto.VascloudService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICountDownLatch;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.grid.ProxyManager;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;

@Component
@Scope("prototype")
public class CampaignSmsSubmitCallable implements Callable<Long> {

    private final Logger log = LoggerFactory.getLogger(CampaignSmsSubmitCallable.class);
    
    private final Logger logstash = LoggerFactory.getLogger("com.ft.logstash.sms.CampaignSmsSubmitCallable");
    
    private final Logger smslogger = LoggerFactory.getLogger("com.ft.csv.sms.CampaignSmsSubmitCallable");

	@Autowired
	CampaignSmsRepository smsRepo;

    @Autowired
    SmsCampaignRepository cpRepo;
    
    @Autowired
    VascloudEventService vascloudEventService;
    
    @Autowired
    @Qualifier("xmlRestTemplate")
	RestTemplate xmlRestTemplate;

    @Autowired
    ObjectMapper mapper;
    
    @Autowired
    ProxyManager<String> bucketProxyManager;
    
    @Autowired
    HazelcastInstance hazelcastInstance;
    
    @Autowired
    RateLimitConfiguration rateLimitConfiguration;
    
    public static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HHmm"); 
    
    public CampaignSmsSubmitCallable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CampaignSmsSubmitCallable(Iterable<CampaignSms> smsList, SmsCampaign campaign) {
		super();
		this.smsList = smsList;
		this.campaign = campaign;
	}

	// ========= RUNTIME ATTRIBUTES ===============
    
    private Iterable<CampaignSms> smsList;

	private SmsCampaign campaign;
	
    @Override
    public Long call() throws Exception {
    	long result = 0;
    	VascloudService service = mapper.readValue(campaign.getVasCloudPackage(), VascloudService.class);
        for (CampaignSms sms : smsList) {
        	checkCampaignTps();
        	if (checkGlobalTps()) {
        		VasCloudSmsRequestDTO request = MessageUtils.createCampaignSmsMessage(sms, service);
        		this.processSms(sms, request);
        		smsRepo.save(sms);
        		result ++;
        	} else {
        		sms.setRequestAt(Instant.now().plusSeconds(300)); // retry after 5 mins
        		sms.setState(HttpStatus.PROCESSING.value());
        		smsRepo.save(sms);
        	}
        }
		return result;
    }
    
    /**
     * Process the SMS and track result
     * @param sms
     * @param request
     */
    public void processSms(CampaignSms sms, VasCloudSmsRequestDTO request) {
    	log.info("Send SMS: {} -> {} : {}", sms.getId(), sms.getMsisdn(), sms.getText());
		try {
			log.debug("SMS VASCLOUD REQUEST:{}", request);
			VasCloudSmsResponseDTO response = vascloudEventService.sendCampaignSms(sms, request);
			sms
			.requestAt(Instant.now())
			;
			
		} catch (HttpClientErrorException s){
			log.error("Cannot send message: {} - {} : {}", s.getRawStatusCode(), s.getStatusText(), s.getResponseBodyAsString());
			log.error("SMS: {}", sms);
		} catch (Exception e) {
		    log.error("Unknown Exception: {}", request, e);
		}
		Metrics.globalRegistry.counter("campaign_sms", Tags.of(
				"campaign", campaign.getName(), 
				"error", "http-" + sms.getState())
		).increment();
		
		smslogger.info("{}", StringUtils.collectionToCommaDelimitedString(Arrays.asList(
				sms.getId(),
				sms.getRequestAt() + "",
				sms.getMsisdn(),
				sms.getState(),
				StringEscapeUtils.escapeCsv(sms.getText()),
				campaign.getName(),
				campaign.getId()
		)));
		
		if (sms.getState() == 202) { // ACCEPTED
			log.debug("SMS mark as success: {}", sms);
			logstash.info("Send Sms: {}", sms);
		} else if (sms.getState() == 406) { // if NOT_ACCEPTED then we should not retry
			log.debug("SMS to discard: {}", sms);
		} else {
			sms.setRequestAt(Instant.now().plusSeconds(3600));
			log.debug("SMS to retry: {} -> 102 @ {}", sms.getState(), sms.getRequestAt());
			sms.setState(102);
		}
		
		sms = smsRepo.save(sms);
    }


	private boolean checkGlobalTps() throws InterruptedException {
    	String hour = "TPS_" + fmt.format(LocalDateTime.now()).substring(0, 3) + "0"; // 0820
    	log.debug("This hour {} tps {}", hour, rateLimitConfiguration.getHourlyTps().get(hour));
    	if (rateLimitConfiguration.getHourlyTps().get(hour) == null) return true;
    	if (rateLimitConfiguration.getHourlyTps().get(hour) == 0) {
    		ICountDownLatch latch = hazelcastInstance.getCountDownLatch("AWAIT_TPS");
    		latch.trySetCount(1);
    		latch.await(10, TimeUnit.MINUTES);
    		return false;
    	} else {
    		bucketProxyManager.getProxy(hour, () -> Bucket4j.configurationBuilder().addLimit(Bandwidth.simple(rateLimitConfiguration.getHourlyTps().get(hour), Duration.ofSeconds(1L))).build())
    		.asScheduler().consume(1L);
    	}
    	return true;
	}

	private void checkCampaignTps() throws InterruptedException {
    	if (campaign.getRateLimit() == null) return;
    	bucketProxyManager.getProxy("campaign-" + campaign.getId(), () -> Bucket4j.configurationBuilder().addLimit(Bandwidth.simple(campaign.getRateLimit(), Duration.ofSeconds(1L))).build()).asScheduler().consume(1L);
	}

    public Iterable<CampaignSms> getSmsList() {
		return smsList;
	}

	public void setSmsList(Iterable<CampaignSms> smsList) {
		this.smsList = smsList;
	}

	public SmsCampaign getCampaign() {
		return campaign;
	}

	public void setCampaign(SmsCampaign campaign) {
		this.campaign = campaign;
	}
	
	@PreDestroy
	public void cleanUp() {
		CampaignSmsProcessorService.activeTasks.remove(campaign.getId());
	}
}
