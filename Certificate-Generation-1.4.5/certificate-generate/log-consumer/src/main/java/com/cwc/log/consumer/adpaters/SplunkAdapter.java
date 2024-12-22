package com.cwc.log.consumer.adpaters;

import com.cwc.log.consumer.adpaters.processor.LogProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/07/14
 */

@Component
@Slf4j
//TODO : Not tested use your own risk(Only Demo code here)
public class SplunkAdapter implements LogProcessor {

    @Value("${splunk.hec.uri}")
    private String splunkUri;

    @Value("${splunk.hec.token}")
    private String splunkToken;

    @Value("${splunk.hec.index}")
    private String splunkIndex;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void captureLogs(String gateway, String logMessage) {
        log.info("Received gateway: {}, logMessage for Splunk : {}", gateway, logMessage);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Splunk " + splunkToken);
            headers.set("Content-Type", "application/json");

            String event = String.format("{\"event\": \"%s\", \"sourcetype\": \"_json\", \"index\": \"%s\", \"gateway\": \"%s\"}", logMessage, splunkIndex, gateway);

            HttpEntity<String> request = new HttpEntity<>(event, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(splunkUri, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Event sent to Splunk successfully: {}", logMessage);
            } else {
                log.error("Failed to send event to Splunk. Status code: {}, Response: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("Error sending event to Splunk", e);
        }
    }
}
