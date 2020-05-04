package com.ft.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.ft.service.util.HeaderRequestInterceptor;
import com.ft.service.util.LoggingInterceptor;

@Configuration
public class RestTemplateConfiguration {
	
	@Autowired
	MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter;
	
	@Bean(name = "jsonRestTemplate")
	RestTemplate jsonRestTemplate() {
		RestTemplate result = new RestTemplate();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		result.getInterceptors().add(new HeaderRequestInterceptor(headers));
		result.getInterceptors().add(new LoggingInterceptor());
		result.getMessageConverters().clear();
		result.getMessageConverters().add(mappingJackson2HttpMessageConverter);
		return result;
	}
	
	@Bean("xmlRestTemplate")
	public RestTemplate xmlRestTemplate() {
		RestTemplate result = new RestTemplate();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
		result.getInterceptors().add(new HeaderRequestInterceptor(headers));
		result.getInterceptors().add(new LoggingInterceptor());
		result.getMessageConverters().clear();
		result.getMessageConverters().add(mappingJackson2XmlHttpMessageConverter);
		return result;
	}
}
