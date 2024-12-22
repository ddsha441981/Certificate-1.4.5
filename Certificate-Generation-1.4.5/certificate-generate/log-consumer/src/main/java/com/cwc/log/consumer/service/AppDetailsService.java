package com.cwc.log.consumer.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/07/14
 */

@Slf4j
@Service
public class AppDetailsService {

    @Value("${spring.profiles.active:}")
    private String activeProfiles;
    @Value("${server.port}")
    private int appPort;
    @Value("${application.title}")
    private String appTitle;
    @Value("${application.version}")
    private String appVersion;
    @Value("${application.appDevelopBy}")
    private String appDevelopBy;
    @PostConstruct
    public void logApplicationDetails() {
        log.info("Application Details: [Port: {}, Name: {}, Version: {}, Developed By: {}, Active Profiles: {}]",
                appPort,
                appTitle,
                appVersion,
                appDevelopBy,
                formatActiveProfiles());
    }

    private String formatActiveProfiles() {
        return Optional.ofNullable(activeProfiles)
                .map(profiles -> Arrays.stream(profiles.split(","))
                        .map(String::trim)
                        .collect(Collectors.joining(", ")))
                .orElse("No active profiles set.");
    }
}
