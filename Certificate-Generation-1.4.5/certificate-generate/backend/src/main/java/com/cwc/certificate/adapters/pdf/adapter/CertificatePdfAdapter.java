package com.cwc.certificate.adapters.pdf.adapter;

import com.cwc.certificate.adapters.pdf.processor.PdfProcessor;
import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.response.CertificateResponse;
import com.cwc.certificate.service.CertificateService;
import com.cwc.certificate.utility.CustomIdGenerator;
import com.cwc.certificate.utility.PdfGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Component
@Slf4j
public class CertificatePdfAdapter implements PdfProcessor {

    private final CertificateService certificateService;
    private final PdfGeneratorService pdfGeneratorService;

    public CertificatePdfAdapter(CertificateService certificateService, PdfGeneratorService pdfGeneratorService) {
        this.certificateService = certificateService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @Override
    public ResponseEntity<byte[]> makePdfProcess(String gateway) {
        log.info(gateway + " Process");
        try {
            List<Map<String, String>> data = getDataForEntityType(gateway);
            //call pdf generating utility service
            byte[] pdfBytes = pdfGeneratorService.generatePdf(data, gateway);

            // Set the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData(ConstantValue.DEFAULT_ATTACHMENT_NAME, gateway + ConstantValue.DEFAULT_PDF_FORMAT_NAME);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Map<String, String>> getDataForEntityType(String entityType) {
        // Reset the ID generator
        CustomIdGenerator.resetId();

        List<Map<String, String>> data = new ArrayList<>();

        if (ConstantValue.DEFAULT_CERTIFICATE_NAME.equals(entityType)) {
            log.info("Certificate pdf file is generating ");
            List<CertificateResponse> certificateResponses = this.certificateService.allCertificates();
            for (CertificateResponse certificates : certificateResponses) {
                Map<String, String> certificateMap = new LinkedHashMap<>();
                int customId = CustomIdGenerator.getNextId();
                certificateMap.put(ConstantValue.DEFAULT_ID_COLUMN, String.valueOf(customId));
                certificateMap.put(ConstantValue.DEFAULT_NAME_COLUMN, certificates.getCandidateName());
                certificateMap.put(ConstantValue.DEFAULT_COMPANY_NAME_COLUMN, certificates.getCompanyName());
                certificateMap.put(ConstantValue.DEFAULT_EMAIL_COLUMN, certificates.getCandidateEmail());
                certificateMap.put(ConstantValue.DEFAULT_JOB_COLUMN, certificates.getJobTitle());

                data.add(certificateMap);
            }
        }
        return data;
    }

}
