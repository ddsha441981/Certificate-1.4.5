package com.cwc.certificate;

import com.cwc.certificate.driven.kafka.SendLogServiceToConsumer;
import com.cwc.certificate.utility.AppDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;



/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/03/17
 */


@SpringBootApplication
@Slf4j
@EnableScheduling
@EnableJpaAuditing
@EnableCaching
@EnableRetry
//@Profile("dev")
@RequiredArgsConstructor
@EnableBatchProcessing
@EnableAsync
public class CertificateApplication implements CommandLineRunner {

    private final AppDetailsService appDetailsService;
    private final SendLogServiceToConsumer sendLogServiceToConsumer;

    public static void main(String[] args) {
        // Load environment properties configuration
//        LoadEnvConfiguration.loadENVPropertiesConfiguration();
        SpringApplication.run(CertificateApplication.class, args);
    }

    @Override
    public void run(String... args) {
        //event trigger to kafka
        sendLogServiceToConsumer.performOperation("Test data for log");
        log.info("-----------------------------App Information-----------------------------------");
        appDetailsService.logApplicationDetails();
    }

}
