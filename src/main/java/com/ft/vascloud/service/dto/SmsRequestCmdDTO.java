package com.ft.vascloud.service.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SmsRequestCmdDTO {

	@JacksonXmlProperty(localName = "transaction_id")
    private String transactionId; //20191209140700010
    
    @JacksonXmlProperty(localName = "mo_id")
    private String moId; //0
    
    @JacksonXmlProperty(localName = "destination_address")
    private String destinationAddress; //84886616291
    
    @JacksonXmlProperty(localName = "source_address")
    private String sourceAddress; //9522
    
    @JacksonXmlProperty(localName = "brandname")
    private String brandname; //9522
    
    @JacksonXmlProperty(localName = "content_type")
    private String contentType; //TEXT
    
    @JacksonXmlProperty(localName = "user_name")
    private String username; //BMTECHMEDIA
    
    @JacksonXmlProperty(localName = "authenticate")
    private String authenticate; //d88a6744fa3b9254966777d933811e71
    
    @JacksonXmlProperty(localName = "info")
    private String info; //Run
    
    @JacksonXmlProperty(localName = "command_code")
    private String commandCode; //MO
    
    @JacksonXmlProperty(localName = "cp_code")
    private String cpCode; //BMTECHMEDIA
    
    @JacksonXmlProperty(localName = "cp_charge")
    private String cpCharge; //BMTECHMEDIA
    
    @JacksonXmlProperty(localName = "service_code")
    private String serviceCode; //RUNMOBILE
    
    @JacksonXmlProperty(localName = "package_code")
    private String packageCode; //NGAY
    
    @JacksonXmlProperty(localName = "package_price")
    private String packagePrice; //0

	@Override
	public String toString() {
		return "SmsRequestCmdDTO [transactionId=" + transactionId + ", moId=" + moId + ", destinationAddress="
				+ destinationAddress + ", sourceAddress=" + sourceAddress + ", brandname=" + brandname
				+ ", contentType=" + contentType + ", username=" + username + ", authenticate=" + authenticate
				+ ", info=" + info + ", commandCode=" + commandCode + ", cpCode=" + cpCode + ", cpCharge=" + cpCharge
				+ ", serviceCode=" + serviceCode + ", packageCode=" + packageCode + ", packagePrice=" + packagePrice
				+ "]";
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMoId() {
		return moId;
	}

	public void setMoId(String moId) {
		this.moId = moId;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public String getBrandname() {
		return brandname;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAuthenticate() {
		return authenticate;
	}

	public void setAuthenticate(String authenticate) {
		this.authenticate = authenticate;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
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

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(String packagePrice) {
		this.packagePrice = packagePrice;
	}

    
}
