package com.ft.vascloud.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("ACCESSGW")
public class VasCloudSmsResponseDTO {

	@JsonProperty("MODULE")
	private String module = "SMSGW";

	@JsonProperty("MESSAGE_TYPE")
    private String msgType = "RESPONSE";

	@JsonProperty("COMMAND")
    private SmsRespCmdDTO cmd = new SmsRespCmdDTO();

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

	public SmsRespCmdDTO getCmd() {
		return cmd;
	}

	public void setCmd(SmsRespCmdDTO cmd) {
		this.cmd = cmd;
	}

	@Override
	public String toString() {
		return "VasCloudSmsResponseDTO [module=" + module + ", msgType=" + msgType + ", cmd=" + cmd + "]";
	}
}
