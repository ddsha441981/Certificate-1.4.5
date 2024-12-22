package com.cwc.certificate.adapters.pdf.adapter;

import com.cwc.certificate.adapters.pdf.processor.PdfProcessor;
import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.response.CompanyResponse;
import com.cwc.certificate.service.CompanyService;
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
public class CompanyPdfAdapter implements PdfProcessor {

    private final CompanyService companyService;
    private final PdfGeneratorService pdfGeneratorService;

    public CompanyPdfAdapter(CompanyService companyService, PdfGeneratorService pdfGeneratorService) {
        this.companyService = companyService;
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

    private List<Map<String, String>> getDataForEntityType(String gateway) {
        // Reset the ID generator
        CustomIdGenerator.resetId();

        List<Map<String, String>> data = new ArrayList<>();
         if (ConstantValue.DEFAULT_COMPANY_NAME.equals(gateway)) {
            log.info("Company pdf file is generating ");
            List<CompanyResponse> generatePdfData = this.companyService.getCompanyList();
            for (CompanyResponse companyResponse : generatePdfData) {
                Map<String, String> companyMap = new LinkedHashMap<>();
                int customId = CustomIdGenerator.getNextId();
                companyMap.put(ConstantValue.DEFAULT_ID_COLUMN, String.valueOf(customId));
                companyMap.put(ConstantValue.DEFAULT_COMPANY_NAME_COLUMN, companyResponse.getCompanyName());
                companyMap.put(ConstantValue.DEFAULT_EMAIL_COLUMN, companyResponse.getCompanyEmail());
                companyMap.put(ConstantValue.DEFAULT_STATUS_COLUMN, String.valueOf(companyResponse.getChangeStatus()));
                data.add(companyMap);
            }
        }
        return data;
    }
}
