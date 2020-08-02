package com.trial.ilegra.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IlegraProperties {

	@Value("${sale.batch.analyzer.folder.in}")
	private String saleBatchAnalyzerFolderIn;
	
	@Value("${sale.batch.analyzer.folder.in.read}")
	private String saleBatchAnalyzerFolderInRead;

	@Value("${sale.batch.analyzer.folder.out}")
	private String saleBatchAnalyzerFolderOut;

	public String getSaleBatchAnalyzerFolderIn() {
		return saleBatchAnalyzerFolderIn;
	}
	public String getSaleBatchAnalyzerFolderOut() {
		return saleBatchAnalyzerFolderOut;
	}
	public String getSaleBatchAnalyzerFolderInRead() {
		return saleBatchAnalyzerFolderInRead;
	}

}
