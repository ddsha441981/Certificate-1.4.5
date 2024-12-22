package com.cwc.log.consumer.adpaters;

import com.cwc.log.consumer.adpaters.processor.LogProcessor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/07/14
 */

@Component
@Slf4j
public class ElasticSearchAdapter implements LogProcessor {
    @Value("${elasticsearch.endpoint}")
    private String elasticsearchEndpoint;

    @Value("${elasticsearch.index}")
    private String indexName;

    @Value("${elasticsearch.username}")
    private String elasticsearchUsername;

    @Value("${elasticsearch.password}")
    private String elasticsearchPassword;

    @Value("${elasticsearch.bulk.size}")
    private int bulkSize;

    private final List<String> logBuffer = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    private final RestTemplate restTemplate;

    @Autowired
    public ElasticSearchAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        log.info("Initializing Log Consumer Service : ");
        checkAndCreateIndex();
    }
    @Override
    public void captureLogs(String gateway,String logMessage) {
        log.info("Received gateway: {}, logMessage for ELK : {}", gateway, logMessage);
        lock.lock();
        try {
            logBuffer.add(formatLogMessage(logMessage));
            if (logBuffer.size() >= bulkSize) {
                flushLogBuffer();
            }
        } finally {
            lock.unlock();
        }
    }

    // Scheduled task every 5 seconds if bufffer not reached their limit
    @Scheduled(fixedRateString = "${elasticsearch.bulk.flush.interval:5000}")
    public void scheduledFlush() {
        lock.lock();
        try {
            if (!logBuffer.isEmpty()) {
                flushLogBuffer();
            }
        } finally {
            lock.unlock();
        }
    }

    private void flushLogBuffer() {
        if (logBuffer.isEmpty()) return;

        log.info("Flushing log buffer with {} logs", logBuffer.size());

        try {
            HttpHeaders headers = createHeaders(elasticsearchUsername, elasticsearchPassword);
            headers.set("Content-Type", "application/x-ndjson");

            StringBuilder bulkRequestBody = new StringBuilder();
            for (String logEntry : logBuffer) {
                bulkRequestBody.append("{ \"index\" : { \"_index\" : \"")
                        .append(indexName)
                        .append("\" } }\n")
                        .append(logEntry)
                        .append("\n");
            }

            HttpEntity<String> requestEntity = new HttpEntity<>(bulkRequestBody.toString(), headers);
            String url = elasticsearchEndpoint + "/_bulk";

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Bulk log request sent successfully to Elasticsearch");
            } else {
                log.error("Failed to send bulk log request: {}", response.getStatusCode());
            }

        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("Unauthorized access to Elasticsearch. Check your credentials.");
        } catch (HttpClientErrorException e) {
            log.error("Client error when sending logs to Elasticsearch: {}", e.getStatusCode());
        } catch (Exception e) {
            log.error("Error sending logs to Elasticsearch: {}", e.getMessage(), e);
        } finally {
            logBuffer.clear();
        }
    }

    private String formatLogMessage(String logMessage) {
        String timestamp = DateTimeFormatter.ISO_INSTANT
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        return String.format("{\"message\": \"%s\", \"timestamp\": \"%s\"}", escapeJson(logMessage), timestamp);
    }

    private void checkAndCreateIndex() {
        try {
            String url = String.format("%s/%s", elasticsearchEndpoint, indexName);
            HttpHeaders headers = createHeaders(elasticsearchUsername, elasticsearchPassword);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.HEAD,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Index '{}' already exists.", indexName);
            } else {
                log.info("Index '{}' does not exist. Creating index...", indexName);
                createIndex();
            }
        } catch (HttpClientErrorException.NotFound e) {
            log.info("Index '{}' not found. Creating index...", indexName);
            createIndex();
        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("Unauthorized access when checking index existence. Check your credentials.");
        } catch (Exception e) {
            log.error("Error checking index existence: {}", e.getMessage(), e);
        }
    }


    private void createIndex() {
        try {
            String url = String.format("%s/%s", elasticsearchEndpoint, indexName);
            HttpHeaders headers = createHeaders(elasticsearchUsername, elasticsearchPassword);
            headers.set("Content-Type", "application/json");

            String jsonBody = """
                    {
                      "settings": {
                        "number_of_shards": 1,
                        "number_of_replicas": 1
                      },
                      "mappings": {
                        "properties": {
                          "message": {
                            "type": "text"
                          },
                          "timestamp": {
                            "type": "date"
                          }
                        }
                      }
                    }""";

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Index '{}' created successfully.", indexName);
            } else {
                log.error("Failed to create index '{}': {}", indexName, response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            log.error("Client error when creating index '{}': {}", indexName, e.getStatusCode());
        } catch (Exception e) {
            log.error("Error creating index '{}': {}", indexName, e.getMessage(), e);
        }
    }

    private HttpHeaders createHeaders(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return headers;
    }

    private String escapeJson(String text) {
        return text.replace("\"", "\\\"");
    }
}
