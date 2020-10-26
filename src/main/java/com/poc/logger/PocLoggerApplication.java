package com.poc.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class PocLoggerApplication {

	@Autowired
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(PocLoggerApplication.class, args);
	}

}
