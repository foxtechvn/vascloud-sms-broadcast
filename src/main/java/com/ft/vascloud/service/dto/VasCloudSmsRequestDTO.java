package com.ft.vascloud.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("ACCESSGW")
public class VasCloudSmsRequestDTO {

	@JsonProperty("MODULE")
	private String module = "SMSGW";

	@JsonProperty("MESSAGE_TYPE")
    private String msgType = "REQUEST";

	@JsonProperty("COMMAND")
    private SmsRequestCmdDTO cmd = new SmsRequestCmdDTO();

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public SmsRequestCmdDTO getCmd() {
		return cmd;
	}

	public void setCmd(SmsRequestCmdDTO cmd) {
		this.cmd = cmd;
	}

	@Override
	public String toString() {
		return "VasCloudSmsRequestDTO [module=" + module + ", msgType=" + msgType + ", cmd=" + cmd + "]";
	}
}
