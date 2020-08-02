package com.trial.ilegra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IlegraApplication {

	public static void main(String[] args) {
		SpringApplication.run(IlegraApplication.class, args);
	}

}
