package com.cwc.log.consumer.adpaters;

import com.cwc.log.consumer.adpaters.processor.LogProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GrafanaAdapter implements LogProcessor {

    @Value("${loki.endpoint.url}")
    private String lokiUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public GrafanaAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void captureLogs(String gateway, String logMessage) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            String payload = String.format(
                    "{ \"streams\": [{ \"stream\": { \"gateway\": \"%s\" }, \"values\": [[\"%d\", \"%s\"]] }] }",
                    gateway,
                    System.currentTimeMillis() * 1000000,
                    logMessage.replace("\"", "\\\"")
            );
            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(lokiUrl, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Log sent to Loki successfully: {}", logMessage);
            } else {
                log.error("Failed to send log to Loki. Status code: {}, Response: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("Error sending log to Loki", e);
        }
    }
}
