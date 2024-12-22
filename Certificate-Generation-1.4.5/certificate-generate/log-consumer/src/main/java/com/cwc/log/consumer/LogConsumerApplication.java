package com.cwc.log.consumer;

import com.cwc.log.consumer.service.AppDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/07/14
 */

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
//@EnableScheduling
public class LogConsumerApplication implements CommandLineRunner {

	private final AppDetailsService appDetailsService;

	public static void main(String[] args) {
		SpringApplication.run(LogConsumerApplication.class, args);
	}
	@Override
	public void run(String... args) {
		log.info("-----------------------------Log Consumer Service-----------------------------------");
		appDetailsService.logApplicationDetails();
	}
}
