package com.thewayhome.ptis.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {"com.thewayhome.ptis.core", "com.thewayhome.ptis.batch"})
public class PtisBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtisBatchApplication.class, args);
	}

}
