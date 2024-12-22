package com.cwc.certificate.service;

import org.springframework.http.ResponseEntity;
/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

public interface ExcelService {
     ResponseEntity<byte[]> processExportDataToExcel(String gateway);
}
