package com.taikang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MongoDBApplication {

	private static final Logger logger = LoggerFactory.getLogger(MongoDBApplication.class);
	
	public static void main(String[] args) {
		
		logger.info("### MongoDBApplication start ...");
		SpringApplication.run(MongoDBApplication.class, args);

	}

}
