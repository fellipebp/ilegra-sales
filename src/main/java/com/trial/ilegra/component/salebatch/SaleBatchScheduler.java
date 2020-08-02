package com.trial.ilegra.component.salebatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SaleBatchScheduler {

	@Autowired
	private SaleBatchAnalyzer saleBatchAnalyzer;
	
	@Scheduled(fixedDelayString = "${sale.batch.analyzer.scheduler}")
	public void run() {
		saleBatchAnalyzer.run();
	}
}
