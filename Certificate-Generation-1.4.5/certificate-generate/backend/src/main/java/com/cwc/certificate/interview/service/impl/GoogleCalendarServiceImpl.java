package com.cwc.certificate.interview.service.impl;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.interview.model.InterviewEvent;
import com.cwc.certificate.interview.service.GoogleCalendarService;
import com.cwc.certificate.utility.GoogleAppCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/10/31
 * HAPPY DIWALI
 */

@Service
@Slf4j
public class GoogleCalendarServiceImpl  implements GoogleCalendarService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final GoogleAppCache googleAppCache;

    @Autowired
    public GoogleCalendarServiceImpl(ObjectMapper objectMapper, RestTemplate restTemplate, GoogleAppCache googleAppCache) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.googleAppCache = googleAppCache;
    }

    @Override
    public String createEvent(InterviewEvent event) {
        String requestBody = constructEventRequestBody(event);
        log.info("-----------------------Create Trigger-------------------------");
        String response = sendRequestToGoogleScript("create", requestBody);
        return response;
    }

    @Override
    public String cancelEvent(InterviewEvent event) {
        log.info("-----------------------Cancel Trigger-------------------------");
        String requestBody = constructEventRequestBody(event);
        String cancel = sendRequestToGoogleScript("cancel", requestBody);
        return cancel;
    }

    @Override
    public String removeEvent(InterviewEvent event) {
        String requestBody = constructEventRequestBody(event);
        log.info("-----------------------Remove Trigger-------------------------");
        String remove = sendRequestToGoogleScript("remove", requestBody);
        return remove;
    }

    @Override
    public String rescheduleEvent(InterviewEvent event) {
        String requestBody = constructEventRequestBody(event);
        log.info("-----------------------Reschedule Trigger-------------------------");
        String reschedule = sendRequestToGoogleScript("reschedule", requestBody);
        return reschedule;
    }

    private String constructEventRequestBody(InterviewEvent event) {
        try {
            String jsonString = objectMapper.writeValueAsString(event);
            log.info("Serialized JSON: " + jsonString);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private String sendRequestToGoogleScript(String action, String requestBody) {
        String SCRIPT_URL = googleAppCache.googleAppCache.get(ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_ID_INTERVIEW);
        String INTERVIEW_SCRIPT_URL = ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL_TYPE + SCRIPT_URL + ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL_SEPARATE + ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL;
        String encodedJsonData = UriComponentsBuilder.fromUriString(INTERVIEW_SCRIPT_URL)
                .queryParam("action", action)
                .queryParam("jsonData", requestBody)
                .build()
                .encode()
                .toUriString();
        log.info("Encoded JSON Data: {}", encodedJsonData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(encodedJsonData, HttpMethod.GET, requestEntity, String.class);
        log.info("Response from Google Script: {}", response.getBody());
        return response.getBody();
    }
}