package com.ft.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.PropertyPlaceholderHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.config.ApplicationProperties;
import com.ft.domain.SmsCampaign;
import com.ft.service.util.JsonUtil;

import io.github.jhipster.config.JHipsterProperties;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
    }
    
    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    ApplicationProperties props;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}': {}", to, content.substring(0, 20) + "...");
        }  catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    private static final PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");
    /**
     * Send Email to user with custom content
     * @param to
     * @param subject
     * @param content
     * @param isMultipart
     * @param isHtml
     * @param placeholders
     */
	@Async
	public void sendEmail(
			String to, 
			String subject, 
			String content, 
			boolean isMultipart, 
			boolean isHtml,
			Map<String, Object> placeholders
	) {
		log.debug(
				"Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={} with placeholder={}",
				isMultipart, isHtml, to, subject, content, placeholders);

		// Prepare message using a Spring helper
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
			message.setTo(to);
			message.setFrom(jHipsterProperties.getMail().getFrom());
			message.setSubject(subject);
			
			Properties properties = new Properties();
			properties.putAll(placeholders);
			properties.put(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
			String messageText = propertyPlaceholderHelper.replacePlaceholders(content, properties);
			message.setText(messageText);
			log.debug("Sent email to User '{}': {}", to, messageText);
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.warn("Email could not be sent to user '{}'", to, e);
			} else {
				log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
			}
		}
	}
	
	/**
	 * Send a HTML email to `to` with subject, content and placeholders
	 * @param to
	 * @param subject
	 * @param content
	 * @param jsonPlaceholders
	 */
	@Async
	public void sendEmail(
			String to, 
			String subject, 
			String content, 
			String jsonPlaceholders
	) {
		log.debug(
				"Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={} with placeholder={}",
				true, true, to, subject, content, jsonPlaceholders);

		// Prepare message using a Spring helper
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
			message.setTo(to);
			message.setFrom(jHipsterProperties.getMail().getFrom());
			message.setSubject(subject);
			
			Properties properties = new Properties();
			properties.putAll(toPlaceholders(jsonPlaceholders));
			properties.put(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
			
			StrSubstitutor strsub = new StrSubstitutor(properties);
			String messageText = strsub.replace(content);
			message.setText(messageText, true);
			log.debug("Sent email to User '{}': {}", to, messageText);
			javaMailSender.send(mimeMessage);
			log.info("+ Email sent: {} -> {} : {}", subject, to, messageText);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.warn("Email could not be sent to user '{}'", to, e);
			} else {
				log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
			}
		}
		
	}
	
	/**
	 * Convert a JSON string into Map with flatten attributes
	 * @param json
	 * @return
	 */
	public Map<String, String> toPlaceholders(String json) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			JsonUtil.plainToFlattenObject("", objectMapper.readTree(json), map);
		} catch (IOException e) {
			log.warn("Cannot flatten json to placeholders", e);
		}
		return map;
	}
	
	/**
	 * Convert nested object into flatten one using toString
	 * @param json
	 * @return
	 */
	public Map<String, String> toPlaceholders(Map<String, Object> json) {
		try {
			return toPlaceholders(objectMapper.writeValueAsString(json));
		} catch (Exception e) {
			log.debug("Cannot flatten object", e);
		}
		return json.entrySet().stream().collect(Collectors.toMap(Entry::getKey, v -> v.getValue().toString()));
	}
	
	/**
	 * Send a notification regards of this campaign email
	 * @param cp
	 */
	public void sendCampaignNotification(SmsCampaign cp) {
		try {
			String jsonString = objectMapper.writeValueAsString(cp);
			String content = props.getEmailTemplates().getOrDefault("campaign", "Campaign ${name} is finished sending.");
			sendEmail("support@hohuymedia.vn", "Campaign Finished", content, jsonString);
		} catch (JsonProcessingException e) {
			log.error("Cannot encode campaign metadata: {}", e);
		}
	}
}
