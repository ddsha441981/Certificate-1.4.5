package com.cwc.certificate.service.impl.googleservice;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.request.ActivityLogRequest;
import com.cwc.certificate.dto.response.CertificateResponse;
import com.cwc.certificate.dto.response.CompanyResponseWithCertificate;
import com.cwc.certificate.exceptions.model.CompanyNotFoundException;
import com.cwc.certificate.model.Certificate;
import com.cwc.certificate.model.Company;
import com.cwc.certificate.observer.Message;
import com.cwc.certificate.observer.MessagePublisher;
import com.cwc.certificate.repository.CertificateRepository;
import com.cwc.certificate.repository.CompanyRepository;
import com.cwc.certificate.service.ActivityLogService;
import com.cwc.certificate.service.CertificateService;
import com.cwc.certificate.service.DocumentService;
import com.cwc.certificate.utility.GoogleAppCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/02/14
 */

@Service
@Slf4j
@PropertySource("classpath:google_url.properties")
public class DocumentServiceImpl implements DocumentService {
    private final CertificateRepository certificateRepository;
    private final CertificateService certificateService;
    private final RestTemplate restTemplate;
    private final GoogleAppCache googleAppCache;
    @Value("${google_url.script}")
    private String scriptUrl;
    private final ObjectMapper objectMapper;
    private final RetryTemplate retryTemplate;
    private final ActivityLogService activityLogService;
    private final MessagePublisher publisher;
    private final CompanyRepository companyRepository;

    @Autowired
    public DocumentServiceImpl(CertificateRepository certificateRepository, CertificateService certificateService, RestTemplate restTemplate, GoogleAppCache googleAppCache, ObjectMapper objectMapper, RetryTemplate retryTemplate, ActivityLogService activityLogService, MessagePublisher publisher, CompanyRepository companyRepository) {
        this.certificateRepository = certificateRepository;
        this.certificateService = certificateService;
        this.restTemplate = restTemplate;
        this.googleAppCache = googleAppCache;
        this.objectMapper = objectMapper;
        this.retryTemplate = retryTemplate;
        this.activityLogService = activityLogService;
        this.publisher = publisher;
        this.companyRepository = companyRepository;
        //Subscribe Observer
        publisher.attach(activityLogService);
    }

    @Override
    @Transactional
    public String callGoogleAppsScriptFunctionUsingCache(int certificateId) {
        Optional<Certificate> certificate = certificateRepository.findById(certificateId);
        String selectedCompanyId = certificate.get().getSelectedCompany();
        int companyId = Integer.parseInt(selectedCompanyId);
        System.out.println(selectedCompanyId);
        Company company = this.companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException("Company not found with this company Id "));
        CompanyResponseWithCertificate responseWithCertificate = buildWithCompanyResponseWithCertificate(company);
        if (certificate.isPresent()) {
            Certificate certificate1 = certificate.get();
            log.info("Certificate Type: {}", certificate1.getSelectedDocumentTypes());
        } else {
            log.info("Certificate not found with this Id: {}", certificateId);
            return "Certificate not found with this Id: " + certificateId;
        }

        // Get data from the database
        CertificateResponse certificateResponse = certificateService.getCertificateId(certificateId);
        System.out.println("----------------------------------" + responseWithCertificate.toString());
        certificateResponse.setCompany(responseWithCertificate);

        if (certificateResponse == null) {
            log.info("Certificate not found with this Id: {}", certificateId);
            return "Certificate not found with this Id: " + certificateId;
        }

        // Convert data into JSON format
        String jsonData;
        try {
            String jsonDataOld = objectMapper.writeValueAsString(certificateResponse);
            // Remove "documentData" from "identificationDetails" in the JSON
            ObjectNode jsonObject = (ObjectNode) objectMapper.readTree(jsonDataOld);
            JsonNode identificationDetailsNode = jsonObject.get("identificationDetails");
            if (identificationDetailsNode != null && identificationDetailsNode.isObject()) {
                ((ObjectNode) identificationDetailsNode).remove("documentData");
            }
            jsonData = objectMapper.writeValueAsString(jsonObject);

            log.info("After Update: {}", jsonData);
        } catch (JsonProcessingException e) {
            log.error("Error converting data to JSON: {}", e.getMessage());
            return "Error converting data to JSON";
        }

        // Get the script URL from the cache
        String script = googleAppCache.googleAppCache.get(ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_CACHE);
        String staticScript = ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL_TYPE + script + ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL_SEPARATE + ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL;
        log.info("Loading Script: {}", staticScript);

        // Encode data into URL
        String encodedJsonData = UriComponentsBuilder.fromUriString(staticScript)
                .queryParam("jsonData", jsonData)
                .build()
                .encode()
                .toUriString();

        log.info("Encoded JSON Data: {}", encodedJsonData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        // Call google apps script function with retry
        try {
            return retryTemplate.execute(
                    context -> {
                        ResponseEntity<String> response = restTemplate.exchange(encodedJsonData, HttpMethod.GET, requestEntity, String.class);

                        //Notify Observer
                        publisher.notifyUpdate(new Message("Document generation process trigger notification"));

                        // Log the action in ActivityLog
                        log.info("Activity log trigger to capture document details...");
                        ActivityLogRequest activityLogRequest = ActivityLogRequest.builder()
                                .actionType("Document Generated")
                                .entityId(certificate.get().getCertificateId())
                                .entityName(certificate.get().getCompanyName())
                                .performedBy(certificate.get().getCandidateName())
                                .actionTimestamp(LocalDateTime.now())
                                .details("Generated document for candidate: " + certificate.get().getCandidateName()
                                        + " of company: " + certificate.get().getCompanyName()
                                        + " on date: " + LocalDateTime.now().toString()
                                        + ". Document generated successfully.")
                                .build();
                        activityLogService.logActivity(activityLogRequest);

                        log.info("Response: {}", response.getBody());

                        if (response.getStatusCode() != HttpStatus.OK) {
                            log.error("Error calling Google Apps Script: HTTP status {}", response.getStatusCode());
                            throw new RuntimeException("Error calling Google Apps Script");
                        }
                        //Detached Observer
                        publisher.detach(activityLogService);
                        return response.getBody();
                    },
                    context -> {
                        // Recovery callback
                        log.error("All retry attempts failed. Providing fallback response.");
                        return "Failed to call Google Apps Script after multiple attempts";
                    }
            );
        } catch (Exception e) {
            log.error("Error processing response: {}", e.getMessage());
            return "Error processing response";
        }
    }

    private CompanyResponseWithCertificate buildWithCompanyResponseWithCertificate(Company company) {
        return CompanyResponseWithCertificate.builder()
                .hrDetails(company.getHrDetails())
                .managerDetails(company.getManagerDetails())
                .companyRevenue(company.getCompanyRevenue())
                .companyFounder(company.getCompanyFounder())
                .companySize(company.getCompanySize())
                .companyEmail(company.getCompanyEmail())
                .companyPhone(company.getCompanyPhone())
                .yearOfEstablishment(company.getYearOfEstablishment())
                .companyRegistrationNumber(company.getCompanyRegistrationNumber())
                .companyDomainType(company.getCompanyDomainType())
                .companyWebsite(company.getCompanyWebsite())
                .addresses(company.getAddresses())
                .addressType(company.getAddressType())
                .industryType(company.getIndustryType())
                .companyLicenseNumber(company.getCompanyLicenseNumber())
                .build();
    }

    //Using Properties File
    public String callGoogleAppsScriptFunction(int certificateId) {
        Optional<Certificate> certificate = certificateRepository.findById(certificateId);
        if (certificate.isPresent()) {
            Certificate certificate1 = certificate.get();
            log.info("Certificate Type: {}", certificate1.getSelectedDocumentTypes());
        } else {
            log.info("Certificate not found with this Id: {}", certificateId);
            return "Certificate not found with this Id: {}: " + certificateId;
        }

        String stringWithoutBackslashes = scriptUrl.replace("\\\\", "");
        log.info("URL without backslashes: {}", stringWithoutBackslashes);

        // get data from the database
        CertificateResponse entity = this.certificateService.getCertificateId(certificateId);

        if (entity == null) {
            log.info("Certificate not found with this Id: {}", certificateId);
//            return new ResourceNotFoundException("Certificate not found with this Id: " + certificateId).toString();
            return "Certificate not found with this Id: {}: " + certificateId;
        }
        //  data convert into json format
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData;
        try {
            jsonData = objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            log.info("Error converting data to JSON", e);
            return "Error converting data to JSON";
        }
        //data encoded in json
        String encodedJsonData = UriComponentsBuilder.fromUriString(stringWithoutBackslashes)
                .queryParam("jsonData", jsonData)
                .build()
                .encode()
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        // call Google apps script function
        ResponseEntity<String> response = restTemplate.exchange(encodedJsonData, HttpMethod.GET, requestEntity, String.class);
        log.info("Response: {}", response.getBody());
        return response.getBody();
    }
}

