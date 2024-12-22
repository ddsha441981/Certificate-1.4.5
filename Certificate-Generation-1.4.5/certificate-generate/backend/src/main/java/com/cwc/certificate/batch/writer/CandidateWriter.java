package com.cwc.certificate.batch.writer;

import com.cwc.certificate.dto.response.CertificateResponse;
import com.cwc.certificate.service.CertificateService;
import com.cwc.certificate.service.CompanyService;
import com.cwc.certificate.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/07
 */

@Component
@Slf4j
public class CandidateWriter implements ItemWriter<String> {

    private final DocumentService documentService;
    private final CertificateService certificateService;
    private final CompanyService companyService;

    @Autowired
    public CandidateWriter(DocumentService documentService,CertificateService certificateService, CompanyService companyService) {
        this.documentService = documentService;
        this.certificateService = certificateService;
        this.companyService = companyService;
    }

    private List<String> responses = new ArrayList<>();

    @Override
    public void write(Chunk<? extends String> certificateIds) {
        certificateIds.forEach(certificateId -> {
            log.info("Writing candidate data for certificateId: " + certificateId);
            int certiId = Integer.parseInt(certificateId);
            String response = this.documentService.callGoogleAppsScriptFunctionUsingCache(certiId);
                log.info("Processing was successful for certificateId: " + certiId);
                responses.add("Certificate ID " + certiId + ": Success");

            CertificateResponse certificateResponse = certificateService.getCertificateId(certiId);
            int id = certificateResponse.getCertificateId();
            String companyName = certificateResponse.getCompanyName();
            String status = String.valueOf(certificateResponse.getChangeStatus());

            //TODO: Make condition like sometime documents not generated and failed to generate then this api call need to analysis how to prevent
            //update certificate status
            this.certificateService.changeCertificateStatus(id,status);
            //update company db
            updateCompaniesData(id,companyName);
            responses.add(response);
        });
    }

    private void updateCompaniesData(int certificateId , String companyName) {
        try {
            Thread.sleep(2000);
            this.certificateService.changeCountCompaniesAfterGeneratingCertificate(certificateId,companyName);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getResponses() {
        return responses;
    }
}

