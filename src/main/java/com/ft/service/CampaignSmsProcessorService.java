package com.ft.service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ft.config.RateLimitConfiguration;
import com.ft.domain.SmsCampaign;
import com.ft.domain.CampaignSms;
import com.ft.repository.SmsCampaignRepository;
import com.ft.repository.CampaignSmsRepository;
import com.ft.vascloud.components.CampaignSmsSubmitCallable;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICountDownLatch;

@Service
public class CampaignSmsProcessorService {

	private static final Logger log = LoggerFactory.getLogger(CampaignSmsProcessorService.class);

	@Autowired
	CampaignSmsRepository campaignSmsRepository;

	@Autowired
	SmsCampaignRepository campaignRepo;

	@Autowired
	ApplicationContext appContext;

	@Autowired
	RateLimitConfiguration rateLimitConfiguration;

	@Autowired
	HazelcastInstance hazelcastInstance;

	@Autowired
	MailService mailService;
	

	public static Map<Long, CampaignSmsSubmitCallable> activeTasks = new ConcurrentHashMap<>();

	public static Map<Long, Future<Long>> futures = new ConcurrentHashMap<>();
	
	/**
	 * Perform send SMS per campaign on separate Callable
	 * @throws InterruptedException 
	 */
	@Scheduled(fixedDelay = 1000)
	public long sendSmsForCampaigns() {
		String hour = LocalDateTime.now().getHour() + "";
		Long tps = rateLimitConfiguration.getHourlyTps().get(hour);
		if ((tps != null) && (tps == 0)) {
			awaitForMore();
			return 0;
		}
		long result = 0L;
		ExecutorService executorService = Executors.newFixedThreadPool(4);
		ExecutorCompletionService<Long> executorCompletionService = new ExecutorCompletionService<Long>(
				executorService);
		// Then again we can submit the tasks and get the result like:
		cleanupFutures();
		
		for (SmsCampaign cp : campaignRepo.findAllByState(HttpStatus.MOVED_PERMANENTLY.value())) {
			enqueueSmsForCampaign(executorCompletionService, cp);
		}
		for (int i = 0; i < futures.size(); i++) {
			result = awaitCompletion(result, executorCompletionService);
		}
		if (result > 0) {
			log.info("+ {} SMS sent out", result);
		} else {
			awaitForMore();
		}
		return result;
	}

	/**
	 * Try to lock the current thread and wait for more message to run
	 */
	private void awaitForMore() {
		ICountDownLatch latch = hazelcastInstance.getCountDownLatch("AWAIT_ACTIVE_CAMPAIGN");
		latch.trySetCount(1);
		try {
			latch.await(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Try to lock the thread until all the campaign have finish running
	 * @param result
	 * @param executorCompletionService
	 * @return
	 */
	private long awaitCompletion(long result, ExecutorCompletionService<Long> executorCompletionService) {
		try {
			result = result + executorCompletionService.take().get();
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		} catch (ExecutionException e) {
			log.error("Execution Failed", e);
		}
		return result;
	}

	private void enqueueSmsForCampaign(ExecutorCompletionService<Long> executorCompletionService, SmsCampaign cp) {
		Page<CampaignSms> smsList = campaignSmsRepository.findAllByCampaignAndState(cp, HttpStatus.PROCESSING.value(), PageRequest.of(0, 10000));
		// Lock current SMS list for further processing
		long locked = 0L;
		for (CampaignSms sms : smsList) {
			locked += 1;
			campaignSmsRepository.save(sms.state(HttpStatus.LOCKED.value()));
		}
		if (locked > 0) {
			// Submit the SMS list into separate thread for further processing
			CampaignSmsSubmitCallable task = appContext.getBean(CampaignSmsSubmitCallable.class);
			task.setCampaign(cp);
			task.setSmsList(smsList);
			futures.put(cp.getId(), executorCompletionService.submit(task));
			activeTasks.put(cp.getId(), task);
		} else {
			log.debug("== Campaign is mark as idle: {}", cp.getName());
		}
	}

	/**
	 * Remove futures those are left
	 */
	public void cleanupFutures() {
		for (Iterator<Future<Long>> it = futures.values().iterator(); it.hasNext();) {
			Future<Long> e = it.next();
			if (e.isCancelled() || e.isDone()) {
				it.remove();
			}
		}
	}
}
