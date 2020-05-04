package com.ft.vascloud.service.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SmsRespCmdDTO {
	
	@JacksonXmlProperty(localName = "error_id")
	private int errorId = 0; // 0
	
	@JacksonXmlProperty(localName = "error_desc")
	private String errorDesc = "Successful"; // successfully

	@Override
	public String toString() {
		return "SmsRespCmdDTO [errorId=" + errorId + ", errorDesc=" + errorDesc + "]";
	}

	public int getErrorId() {
		return errorId;
	}

	public void setErrorId(int errorId) {
		this.errorId = errorId;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	
	
	
}
