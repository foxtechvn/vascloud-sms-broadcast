package com.ft.service;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ft.config.ApplicationProperties;
import com.ft.config.MinioConfiguration;
import com.ft.domain.SmsCampaign;
import com.ft.domain.CampaignSms;
import com.ft.domain.QCampaignSms;
import com.ft.repository.SmsCampaignRepository;
import com.ft.repository.CampaignSmsRepository;
import com.ft.service.CampaignImportService;

import io.minio.MinioClient;

@Service
public class CampaignImportService {

	private final Logger log = LoggerFactory.getLogger(CampaignImportService.class);
	
	
	@Autowired
	MinioClient minioClient;
	
	@Autowired
	MinioConfiguration minioConfig;
	
	@Autowired
	CampaignSmsRepository smsRepo;
	
	@Autowired
	SmsCampaignRepository cpRepo;
	
	@Autowired
	ApplicationProperties props;
	
	/**
	 * Perform import file taken from minio, return a list of MSISDN to be import
	 * @param filename
	 * @throws Exception 
	 */
	public void importFile(String filename, SmsCampaign cp) throws Exception {
		InputStream is = minioClient.getObject(minioConfig.getBucketName(), filename);
		File file = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
		FileUtils.copyToFile(is, file);
		is.close();
		String mimeType = Files.probeContentType(file.toPath());
		log.debug("Gotta process file of type: {}", mimeType);
		performCsvImport(file, cp);
//		cp.getAttachments().remove(filename);
//		cp.getTags().add(filename);
		cpRepo.save(cp);
		log.info("+ Processed datafile {} for campaign {} {}", filename, cp.getId(), cp.getName());
	}


	/**
	 * Process campaign xlsx file import
	 * campaign.meta.headers = "col1, col2, col3"
	 * campaign.meta.pkey = "col1"
	 * campaign.meta.source = "SHORTCODE"
	 * @param file
	 * @param cp
	 * @throws Exception 
	 */
	private void performCsvImport(File file, SmsCampaign cp) throws Exception {
//		log.debug("Gonna import CSV file into campaign: {}", cp);
//		String[] headerColumns = StringUtils.tokenizeToStringArray(cp.getMeta().get("headers").toString(), "\r\n\f\t,", true, true);
//		String pkey = cp.getMeta().get("pkey") == null ? headerColumns[0] : cp.getMeta().get("pkey").toString();
//		for (String line : FileUtils.readLines(file, Charset.defaultCharset())) {
//			try {
//    			String[] row =  StringUtils.commaDelimitedListToStringArray(line);
//    		    Map<String, Object> entry = new HashMap<String, Object>();
//    		    for (int j = 0; j < headerColumns.length; j++) {
//    		    	try {
//    		    		entry.put(headerColumns[j], row[j]);
//    				} catch (Exception e) {
//   						log.warn("Cannot parse cell: {}, {}: {}", row, j, e);
//    				}
//    		    }
//    		    doSaveSms(cp, pkey, entry);
//    		} catch (Exception e) {
//    			log.debug("Cannot import row {}: {}", line, e);
//    		}
//		}
//		// Add the total to this campaign
//    	long total = smsRepo.count(QCampaignSms.campaignSms.requestBy.eq(cp.getId()));
////		cp.getMeta().put("total", total);
//		cpRepo.save(cp);
//		
//		file.delete();
	}
	
	/**
	 * Perform import for embed campaign
	 * @param cp
	 * @throws Exception
	 */
	public void importMsisdnText(SmsCampaign cp) throws Exception {
//		String[] headerColumns = StringUtils.tokenizeToStringArray(cp.getMeta().get("headers").toString(), "\r\n\f\t,", true, true);
//		String pkey = cp.getMeta().get("pkey") == null ? headerColumns[0] : cp.getMeta().get("pkey").toString();
//		for (String line : StringUtils.tokenizeToStringArray(cp.getMeta().get("msisdnText").toString(), "\r\n\f")) {
//			try {
//    			String[] row =  StringUtils.commaDelimitedListToStringArray(line);
//    		    Map<String, Object> entry = new HashMap<String, Object>();
//    		    for (int j = 0; j < headerColumns.length; j++) {
//    		    	try {
//    		    		entry.put(headerColumns[j], row[j]);
//    				} catch (Exception e) {
//   						log.warn("Cannot parse cell: {}, {}: {}", row, j, e);
//    				}
//    		    }
//    		    doSaveSms(cp, pkey, entry);
//    		} catch (Exception e) {
//    			log.debug("Cannot import row {}: {}", line, e);
//    		}
//		}
//		// Add the total to this campaign
//    	long total = smsRepo.count(QCampaignSms.campaignSms.campaign.id.eq(cp.getId()));
//		cp.getMeta().put("total", total);
//		cpRepo.save(cp);
	}

	/**
	 * Perform import one specific campaign
	 * @param cp
	 * @throws Exception
	 */
	public void importDataSource(SmsCampaign cp) throws Exception {
//		if (cp.getDatasource() == 100) {
//			this.importMsisdnText(cp);
//		} else if (cp.getDatasource() == 101) {
//			for (String fileName : cp.getAttachments()) {
//				this.importFile(fileName, cp);
//			}
//		}
	}
	
	/**
	 * Save one SMS
	 * @param cp
	 * @param pkey
	 * @param entry
	 * @return
	 */
	protected CampaignSms doSaveSms(SmsCampaign cp, String pkey, Map<String, Object> entry) {
		String phoneNumber = props.msisdn(entry.get(pkey).toString()).toString();
	    entry.put("msisdn", phoneNumber);
	    entry.put("MSISDN", phoneNumber);
	    entry.put("isdn", "0" + props.isdn(phoneNumber));
	    entry.put("ISDN", "0" + props.isdn(phoneNumber));
	    StrSubstitutor subs = new StrSubstitutor(entry, "{{", "}}");
	    return smsRepo.save(new CampaignSms()
	    		.state(HttpStatus.PROCESSING.value())
	    		.msisdn(phoneNumber)
	    		.requestAt(cp.getStartAt())
	    		.campaign(cp)
	    		.text(subs.replace(cp.getShortMsg()))
	    );
	}
}
