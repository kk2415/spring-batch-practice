package com.batch.practice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class PracticeApplication {

	public static void main(String[] args) {
		log.info("application arguments : " + String.join(", ", args));
		SpringApplication.run(PracticeApplication.class, args);
	}

}
