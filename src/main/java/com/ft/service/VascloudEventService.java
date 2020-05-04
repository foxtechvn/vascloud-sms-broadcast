package com.ft.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ft.config.VascloudProperties;
import com.ft.domain.CampaignSms;
import com.ft.vascloud.service.dto.VasCloudSmsRequestDTO;
import com.ft.vascloud.service.dto.VasCloudSmsResponseDTO;

import io.micrometer.core.annotation.Timed;

@Service
public class VascloudEventService {
	
	private static final Logger log = LoggerFactory.getLogger(VascloudEventService.class);

	@Autowired
	RestTemplate xmlRestTemplate;
	
	@Autowired
    VascloudProperties vascloudProperties;

	@Timed(value = "sms_submit", description = "Number of SMS send out")
	public VasCloudSmsResponseDTO sendCampaignSms(CampaignSms sms, VasCloudSmsRequestDTO request) throws Exception {
		VasCloudSmsResponseDTO response = xmlRestTemplate.postForObject(vascloudProperties.getSmsPushUrl(), request, VasCloudSmsResponseDTO.class);
		log.debug("SMS VASCLOUD RESPONSE:{}", response);
		if (response != null) {
			int errorId =response.getCmd().getErrorId(); 
			if (errorId == 0) {
				log.debug("Submit OK: " + sms);
				sms.state(HttpStatus.ACCEPTED.value());
			} else if ((errorId == 107) || (errorId == 108)) {
				sms.state(HttpStatus.NOT_ACCEPTABLE.value()); // 406 - DO not retry since it not acceptable
				log.debug("Blacklist or invalid MSISDN: {}", sms);
				throw HttpClientErrorException.NotAcceptable.create(HttpStatus.NOT_ACCEPTABLE, response.getCmd().getErrorDesc(), null, null, null);
			} else {
				// Other values we should retry
				log.debug("Rate limit exceeded or should retry");
				if (errorId == 105) throw HttpClientErrorException.Unauthorized.create(HttpStatus.UNAUTHORIZED, response.getCmd().getErrorDesc(), null, null, null);
				throw HttpClientErrorException.TooManyRequests.create(HttpStatus.TOO_MANY_REQUESTS, response.getCmd().getErrorDesc(), null, null, null);
			}
		} else {
			log.error("Submit FAILED: {}", sms);
			sms.state(HttpStatus.UNPROCESSABLE_ENTITY.value());
			throw HttpClientErrorException.UnprocessableEntity.create(HttpStatus.UNPROCESSABLE_ENTITY, "Failed", null, null, null);
		}
		return response;
	}
}
