package com.cwc.certificate.adapters.excel.adapter;

import com.cwc.certificate.adapters.excel.processor.ExcelProcessor;
import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.response.CertificateResponse;
import com.cwc.certificate.service.CertificateService;
import com.cwc.certificate.utility.CustomIdGenerator;
import com.cwc.certificate.utility.ExcelGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
public class CertificateExcelAdapter implements ExcelProcessor {

    private final CertificateService certificateService;
    private final ExcelGeneratorService excelGeneratorService;


    public CertificateExcelAdapter(CertificateService certificateService, ExcelGeneratorService excelGeneratorService) {
        this.certificateService = certificateService;
        this.excelGeneratorService = excelGeneratorService;
    }


    @Override
    public ResponseEntity<byte[]> makeExcelProcess(String gateway) {
        log.info(gateway + " Process");
        try {
            List<Map<String, String>> data = getDataForEntityType(gateway);
            //call excel generating utility service
            byte[] excelBytes = excelGeneratorService.exportDataToExcel(data, gateway);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData(ConstantValue.DEFAULT_ATTACHMENT_NAME, gateway + ConstantValue.DEFAULT_EXCEL_FORMAT_NAME);
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Map<String, String>> getDataForEntityType(String gateway) {
        //Reset Id
        CustomIdGenerator.resetId();
        List<Map<String, String>> data = new ArrayList<>();
        if (ConstantValue.DEFAULT_CERTIFICATE_NAME.equals(gateway)) {
            log.info("Certificate excel file is generating ");
            List<CertificateResponse> certificateList = this.certificateService.allCertificates();
            for (CertificateResponse certificateResponse : certificateList) {
                Map<String, String> certificateMap = new LinkedHashMap<>();
                int customId = CustomIdGenerator.getNextId();
                certificateMap.put(ConstantValue.DEFAULT_ID_COLUMN, String.valueOf(customId));
                certificateMap.put(ConstantValue.DEFAULT_NAME_COLUMN, certificateResponse.getCandidateName());
                certificateMap.put(ConstantValue.DEFAULT_COMPANY_NAME_COLUMN, certificateResponse.getCompanyName());
                certificateMap.put(ConstantValue.DEFAULT_EMAIL_COLUMN, certificateResponse.getCandidateEmail());
                certificateMap.put(ConstantValue.DEFAULT_JOB_COLUMN, certificateResponse.getJobTitle());
                data.add(certificateMap);
            }
        }
        return data;
    }
}
