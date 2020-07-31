package com.trial.ilegra.component;

import org.springframework.scheduling.annotation.Scheduled;

public class SaleBatchScheduler {

	@Scheduled(cron = "sale.batch.analyzer.scheduler")
	public void run() {
		
	}
	
}
