package com.ft.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vascloud", ignoreUnknownFields = false)
public class VascloudProperties {

	private final static Logger log = LoggerFactory.getLogger(VascloudProperties.class);
	
	private String esbUrl = "http://10.144.18.112:8080";
	
	private String ftpUrl = "ftp://10.144.17.78:21";
	
	private String smsMtUrl = "http://10.144.18.112/services/SMS_GW_MT_PROXY?wsdl";
	
	private String smsPushUrl = "http://10.144.18.112/services/SMS_GW_MT_PROXY?wsdl";
	
	private String ccgwUrl = "http://10.144.18.112/services/CHARGING_GW_PROXY?wsdl";
	
	@PostConstruct
	public void init() {
		log.info("Application Properties: {}", toString());
	}

	/** ===================== JAVA GETTER & SETTER =============== */
	
	@Override
	public String toString() {
		return "VascloudProperties [esbUrl=" + esbUrl + ", ftpUrl=" + ftpUrl + ", smsMtUrl=" + smsMtUrl + ", ccgwUrl="
				+ ccgwUrl + "]";
	}

	/**
	 * ESB URL
	 * @return
	 */
	public String getEsbUrl() {
		return esbUrl;
	}

	public void setEsbUrl(String esbUrl) {
		this.esbUrl = esbUrl;
	}

	public String getFtpUrl() {
		return ftpUrl;
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}

	public String getSmsMtUrl() {
		return smsMtUrl;
	}

	public void setSmsMtUrl(String smsMtUrl) {
		this.smsMtUrl = smsMtUrl;
	}

	public String getCcgwUrl() {
		return ccgwUrl;
	}

	public void setCcgwUrl(String ccgwUrl) {
		this.ccgwUrl = ccgwUrl;
	}
	
	public String getSmsPushUrl() {
		return smsPushUrl;
	}

	public void setSmsPushUrl(String smsPushUrl) {
		this.smsPushUrl = smsPushUrl;
	}

	
}
