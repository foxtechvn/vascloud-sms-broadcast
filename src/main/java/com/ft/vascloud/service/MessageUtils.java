package com.ft.vascloud.service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.ft.domain.CampaignSms;
import com.ft.vascloud.service.dto.SmsRequestCmdDTO;
import com.ft.vascloud.service.dto.VasCloudSmsRequestDTO;
import com.ft.vascloud.service.dto.VascloudService;

/**
 * Mapper between messages
 * @author dinhtrung
 *
 */
@Service
public class MessageUtils {

	private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);
	
	public static String getSequenceNumber() {
		return ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replaceAll("^\\d", "");
	}
	
	/*
	 * Create XML request using Sms Campaign instead
	 */
	public static VasCloudSmsRequestDTO createCampaignSmsMessage(CampaignSms sms, VascloudService service)  {
    	log.debug("service: {}", service);
    	VasCloudSmsRequestDTO msg = new VasCloudSmsRequestDTO();
		msg.setMsgType("REQUEST");
		msg.setModule("SMSGW");
		SmsRequestCmdDTO cmd = new SmsRequestCmdDTO();

		long id = System.currentTimeMillis();
		String msisdn = sms.getMsisdn();
		String authenticate = DigestUtils.md5DigestAsHex((
				DigestUtils.md5DigestAsHex((id + service.getSmsUsername()).getBytes())
				+ DigestUtils.md5DigestAsHex(("smsgw@2016" + msisdn).getBytes())
				+ service.getSmsPassword()
		).getBytes()) ;
		
		cmd.setTransactionId("" + id);
		cmd.setAuthenticate(authenticate);
		cmd.setDestinationAddress(msisdn);
		cmd.setSourceAddress(service.getShortCode());
		cmd.setInfo(sms.getText());
		
		cmd.setMoId("0");
		cmd.setBrandname("");
		cmd.setContentType("TEXT");
		cmd.setUsername(service.getSmsUsername());
		cmd.setCommandCode("");
		cmd.setCpCode(service.getCpCode());
		cmd.setCpCharge(service.getCpCharge());
		cmd.setServiceCode(service.getServiceCode());
		// Apply to response messages
		cmd.setPackageCode("PUSH_"  +  service.getSmsPushCode());
		cmd.setPackagePrice("0");
		msg.setCmd(cmd);
		return msg;
	}
}
