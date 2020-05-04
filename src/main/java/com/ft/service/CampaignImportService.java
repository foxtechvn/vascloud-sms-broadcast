package com.ft.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ft.domain.SmsCampaign;

@Service
public interface CampaignImportService {

	@Async
	public void importDataSource(SmsCampaign cp) throws Exception;
}
