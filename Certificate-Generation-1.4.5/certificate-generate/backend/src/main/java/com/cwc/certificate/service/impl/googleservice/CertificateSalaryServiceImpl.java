package com.cwc.certificate.service.impl.googleservice;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.request.ActivityLogRequest;
import com.cwc.certificate.dto.response.CertificateSalaryResponse;
import com.cwc.certificate.exceptions.model.DocumentNotFoundException;
import com.cwc.certificate.exceptions.model.ResourceNotFoundException;
import com.cwc.certificate.model.Certificate;
import com.cwc.certificate.observer.Message;
import com.cwc.certificate.observer.MessagePublisher;
import com.cwc.certificate.repository.CertificateRepository;
import com.cwc.certificate.service.ActivityLogService;
import com.cwc.certificate.service.CertificateSalaryService;
import com.cwc.certificate.utility.GoogleAppCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/02/14
 */

@Service
@Slf4j
public class CertificateSalaryServiceImpl implements CertificateSalaryService {
    private final CertificateRepository certificateRepository;
    private final RestTemplate restTemplate;
    private final GoogleAppCache googleAppCache;
    private final ObjectMapper objectMapper;
    private final RetryTemplate retryTemplate;
    private final ActivityLogService activityLogService;
    private final MessagePublisher publisher;

    @Autowired
    public CertificateSalaryServiceImpl(CertificateRepository certificateRepository, RestTemplate restTemplate, GoogleAppCache googleAppCache, ObjectMapper objectMapper, RetryTemplate retryTemplate, ActivityLogService activityLogService, MessagePublisher publisher) {
        this.certificateRepository = certificateRepository;
        this.restTemplate = restTemplate;
        this.googleAppCache = googleAppCache;
        this.objectMapper = objectMapper;
        this.retryTemplate = retryTemplate;
        this.activityLogService = activityLogService;
        this.publisher = publisher;
        //Subscribe Observer
        publisher.attach(activityLogService);
    }

    //With retry
    @Override
    public String generateSalarySlip(String salaryFrom, String salaryTo, String salaryMode, int certificateId) {
        Certificate certificate = this.certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found with this Id: " + certificateId));

        CertificateSalaryResponse certificateSalaryResponse = CertificateSalaryResponse.builder()
                .certificate(certificate)
                .salaryFrom(salaryFrom)
                .salaryTo(salaryTo)
                .salaryMode(salaryMode)
                .selectedDocumentType(getSelectedDocumentType(certificate))
                .build();
        log.info("Salary generated successfully for certificateId: {}", certificateSalaryResponse.toString());

        String salaryJsonDataOld;
        String salaryJsonData;

        try {
            salaryJsonDataOld = objectMapper.writeValueAsString(certificateSalaryResponse);

            ObjectNode jsonObject = (ObjectNode) objectMapper.readTree(salaryJsonDataOld);
            JsonNode identificationDetailsNode = jsonObject.path(ConstantValue.CERTIFICATE_CARD_NAME_DOCS).path(ConstantValue.IDENTIFICATION_DETAILS);

            // Remove the "documentData" field from "identificationDetails"
            if (identificationDetailsNode.isObject()) {
                ObjectNode identificationDetailsObject = (ObjectNode) identificationDetailsNode;
                identificationDetailsObject.remove(ConstantValue.REMOVED_DATA_DOCS);
            }

            salaryJsonData = objectMapper.writeValueAsString(jsonObject);
            log.info("After Update: {}", salaryJsonData);
        } catch (JsonProcessingException e) {
            log.error("Error converting data to JSON: {}", e.getMessage());
            return "Error converting data to JSON";
        }

        String staticScriptUrl = googleAppCache.googleAppCache.get(ConstantValue.DEFAULT_NAME_SALARY);
        String staticScript = ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL_TYPE + staticScriptUrl + ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL_SEPARATE + ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL;
        String encodedJsonData = UriComponentsBuilder.fromUriString(staticScript)
                .queryParam("responseJson", salaryJsonData)
                .build()
                .encode()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        //Notify Observer
        publisher.notifyUpdate(new Message("Salary generation process trigger notification"));
        // Log the action in ActivityLog
        log.info("Activity log trigger to capture salary details...");
        ActivityLogRequest activityLogRequest = ActivityLogRequest.builder()
                .actionType("Salary Generated")
                .entityId(certificate.getCertificateId())
                .entityName(certificate.getCompanyName())
                .performedBy(certificate.getCandidateName())
                .actionTimestamp(LocalDateTime.now())
                .details("Generated salary for candidate: " + certificate.getCandidateName()
                        + " of company: " + certificate.getCompanyName()
                        + " on date: " + LocalDateTime.now().toString()
                        + ". Salary generated successfully.")
                .build();
        activityLogService.logActivity(activityLogRequest);

        try {
            // Retry logic for HTTP request with recovery
            return retryTemplate.execute(
                    context -> {
                        ResponseEntity<String> response = restTemplate.exchange(encodedJsonData, HttpMethod.GET, requestEntity, String.class);
                        log.info("Response: {}", response.getBody());

                        if (response.getStatusCode() != HttpStatus.OK) {
                            log.error("Error calling Google Apps Script: {}", response.getStatusCode());
                            throw new RuntimeException("Error calling Google Apps Script");
                        }
                            //Detached Observer
                            publisher.detach(activityLogService);
                        return response.getBody();
                    },
                    context -> {
                        // Recovery callback
                        log.error("All retry attempts failed. Providing fallback response.");
                        return "Failed to generate salary slip after multiple attempts";
                    }
            );
        } catch (Exception e) {
            log.error("Error processing response: {}", e.getMessage());
            return "Error processing response";
        }
    }

    // TODO: Need to be change code decide dynamically to get salary docs
    private String getSelectedDocumentType(Certificate certificate) {
        List<String> selectedDocumentTypes = certificate.getSelectedDocumentTypes();
        if (selectedDocumentTypes != null && selectedDocumentTypes.size() >= 3) {
            return selectedDocumentTypes.get(3);//Get 3rd Index of the selectedDocumentTypes
        } else {
            throw new DocumentNotFoundException("Selected document type not found for certificate Id: " + certificate.getCertificateId());
        }
    }
}