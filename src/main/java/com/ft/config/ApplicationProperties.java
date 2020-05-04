package com.ft.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Micro App.
 * <p>
 * Properties are configured in the {@code application.yml} file. See
 * {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	private final static Logger log = LoggerFactory.getLogger(ApplicationProperties.class);

	private String mcc = "84";

	private String gatewayUrl = "http://localhost:8080";
	
	private Map<String, String> emailTemplates = new HashMap<>();

	@PostConstruct
	public void init() {
		log.info("Application Properties: {}", toString());
	}

	@Override
	public String toString() {
		return "ApplicationProperties [mcc=" + mcc + "]";
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	/**
	 * Prepend the MCC part: 09123456789 --> 849123456789
	 *
	 * @param phoneNumber
	 * @return
	 */
	public Long msisdn(String phoneNumber) {
		return Long.parseLong(mcc + isdn(phoneNumber).toString());
	}

	/**
	 * Strip the MCC part : 849123456789 --> 9123456789
	 *
	 * @param phoneNumber
	 * @return
	 */
	public Long isdn(String phoneNumber) {
		return Long.parseLong(phoneNumber.startsWith(mcc) ? phoneNumber.substring(mcc.length()) : phoneNumber);
	}

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	public Map<String, String> getEmailTemplates() {
		return emailTemplates;
	}

	public void setEmailTemplates(Map<String, String> emailTemplates) {
		this.emailTemplates = emailTemplates;
	}
}
