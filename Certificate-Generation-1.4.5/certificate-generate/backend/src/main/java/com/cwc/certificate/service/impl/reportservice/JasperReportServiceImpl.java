package com.cwc.certificate.service.impl.reportservice;

import com.cwc.certificate.service.JasperReportService;

import com.cwc.certificate.model.Certificate;
import com.cwc.certificate.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */


//TODO: No Use

@Slf4j
@Service
@RequiredArgsConstructor
@Deprecated
public class JasperReportServiceImpl implements JasperReportService {
    @Autowired
    private CertificateRepository certificateRepository;

    public String reports(String reportFormat, Integer certificateId) throws FileNotFoundException, JRException {
        String outputPath = "D:\\Report";

        Optional<Certificate> optionalCertificate = certificateRepository.findById(certificateId);
        if (optionalCertificate.isPresent()) {
            Certificate certificate = optionalCertificate.get();

            // Create separate data sources for each report
            JRBeanCollectionDataSource dataSource1 = new JRBeanCollectionDataSource(List.of(certificate));
            JRBeanCollectionDataSource dataSource2 = new JRBeanCollectionDataSource(List.of(certificate));
            JRBeanCollectionDataSource dataSource3 = new JRBeanCollectionDataSource(List.of(certificate));
            JRBeanCollectionDataSource dataSource4 = new JRBeanCollectionDataSource(List.of(certificate));

            // Get JRXML files
            File[] jrxmlFiles = {
                    getJRXMLFile("classpath:report\\EutuxiaTech\\experience.jrxml"),
                    getJRXMLFile("classpath:report\\EutuxiaTech\\relieving_letter.jrxml"),
                    getJRXMLFile("classpath:report\\EutuxiaTech\\apparisal_letter.jrxml"),
                    getJRXMLFile("classpath:report\\EutuxiaTech\\payslip.jrxml")
            };

            for (File jrxmlFile : jrxmlFiles) {
                String fileName = jrxmlFile.getName();
                String outputFileName = fileName.substring(0, fileName.lastIndexOf(".")); // Remove extension

                JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getAbsolutePath());

                Map<String, Object> parameters = new HashMap<>();
                // some data
                parameters.put("Created By:", "infinity corporation");

                // data source for each report
                JasperPrint jasperPrint;
                if (jrxmlFile.getName().equals("experience.jrxml")) {
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource1);
                } else if (jrxmlFile.getName().equals("relieving_letter.jrxml")) {
                    jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource2);
                }else if (jrxmlFile.getName().equals("apparisal_letter.jrxml")) {
                    jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource3);
                } else {
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource4);
                }

                // Output format based on reportFormat
                String outputFile = outputPath + "\\" + outputFileName + "." + reportFormat.toLowerCase();
                if (reportFormat.equalsIgnoreCase("html")) {
                    JasperExportManager.exportReportToHtmlFile(jasperPrint, outputFile);
                } else if (reportFormat.equalsIgnoreCase("pdf")) {
                    JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);
                }
            }
            log.info("Reports generated in path:{}", outputPath);

            return "Reports generated in path: " + outputPath;
        } else {
            throw new IllegalArgumentException("Certificate not found for id: " + certificateId);
        }
    }

    private File getJRXMLFile(String filePath) throws FileNotFoundException {
        return ResourceUtils.getFile(filePath);
    }
}
