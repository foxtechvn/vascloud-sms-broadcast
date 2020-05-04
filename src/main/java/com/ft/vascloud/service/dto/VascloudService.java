package com.ft.vascloud.service.dto;

public class VascloudService {

	private String name;
	
	private String smsPassword;
	
	private String smsUsername;
	
	private String shortCode;
	
	private String cpCode;
	
	private String cpCharge;
	
	private String serviceCode;
	
	private String smsPushCode;

	@Override
	public String toString() {
		return "VascloudService [name=" + name + ", smsPassword=" + smsPassword + ", smsUsername=" + smsUsername
				+ ", shortCode=" + shortCode + ", cpCode=" + cpCode + ", cpCharge=" + cpCharge + ", serviceCode="
				+ serviceCode + ", smsPushCode=" + smsPushCode + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSmsPassword() {
		return smsPassword;
	}

	public void setSmsPassword(String smsPassword) {
		this.smsPassword = smsPassword;
	}

	public String getSmsUsername() {
		return smsUsername;
	}

	public void setSmsUsername(String smsUsername) {
		this.smsUsername = smsUsername;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	public String getCpCharge() {
		return cpCharge;
	}

	public void setCpCharge(String cpCharge) {
		this.cpCharge = cpCharge;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getSmsPushCode() {
		return smsPushCode;
	}

	public void setSmsPushCode(String smsPushCode) {
		this.smsPushCode = smsPushCode;
	}

	
}
