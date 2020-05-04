package com.ft.service.dto;

public class ServiceDTO {

	private String id;
	
	private String serviceId;
	
	private String serviceCode;
	
	private String serviceName;
	
	private String shortcode;

	@Override
	public String toString() {
		return "ServiceDTO [id=" + id + ", serviceId=" + serviceId + ", serviceCode=" + serviceCode + ", serviceName="
				+ serviceName + ", shortcode=" + shortcode + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	
}
