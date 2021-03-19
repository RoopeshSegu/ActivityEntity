package com.tacx.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ActivityEntityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivityEntityApplication.class, args);
	}

}
