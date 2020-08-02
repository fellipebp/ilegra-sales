package com.trial.ilegra.component.salebatch;

import static com.trial.ilegra.component.salebatch.SaleBatchConstants.OUTPUT_FILE;
import static java.lang.System.lineSeparator;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isRegularFile;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.move;
import static java.nio.file.Files.walk;
import static java.nio.file.Files.write;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trial.ilegra.component.salebatch.interpreter.SaleBatchInterpreter;
import com.trial.ilegra.model.dto.ResultSaleDTO;
import com.trial.ilegra.properties.IlegraProperties;

@Component
public class SaleBatchAnalyzer {
	
	private final static Logger LOGGER = Logger.getLogger(SaleBatchAnalyzer.class.getName());
	
	private SaleBatchInterpreter saleBatchInterpreter;
	private IlegraProperties ilegraProperties;

	
	@Autowired
	public SaleBatchAnalyzer(SaleBatchInterpreter saleBatchInterpreter, IlegraProperties ilegraProperties) {
		this.saleBatchInterpreter = saleBatchInterpreter;
		this.ilegraProperties = ilegraProperties;
	}
	
	public void run() {
		analyzer(ilegraProperties.getSaleBatchAnalyzerFolderIn());
	}

	public void analyzer(String path) {
		try (Stream<Path> paths = walk(get(path), 1)) {
			paths.forEach(filePath -> {
				if (isRegularFile(filePath)) {
					try {
						LOGGER.log(FINE, "Reading file: {0}", filePath);
						ResultSaleDTO result = readContent(filePath);
						writeContent(result);
						moveFile(filePath);
					} catch (Exception e) {
						LOGGER.log(SEVERE, e.getMessage());
					}
				}
			});
		} catch (IOException e) {
			LOGGER.log(SEVERE, e.getMessage());
		}
		
	}

	private void moveFile(Path filePath) {
		try {
			String readFolder = ilegraProperties.getSaleBatchAnalyzerFolderInRead();
			if (!exists(get(readFolder))) {
				createDirectories(get(readFolder));
			}
			move(filePath, 
			     get(readFolder.concat(filePath.getFileName().toString())), 
			     StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e) {
			LOGGER.log(SEVERE, e.getMessage());
		}
	}

	private void writeContent(ResultSaleDTO result) {
		try {
			write(get(ilegraProperties.getSaleBatchAnalyzerFolderOut().concat(OUTPUT_FILE)), 
				  result.toString().concat(lineSeparator()).getBytes(), 
				  CREATE, APPEND);
		} catch (IOException e) {
			LOGGER.log(SEVERE, e.getMessage());
		}
	}

	public ResultSaleDTO readContent(Path filePath) throws IOException {
		try (Stream<String> lines = lines(filePath)) {
			return saleBatchInterpreter.process(lines.collect(toList()));
		}catch (Exception e) {
			LOGGER.log(SEVERE, e.getMessage());
			throw new RuntimeException("Error on processing lines.");
		}
	}

}
