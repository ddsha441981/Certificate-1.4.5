package com.cwc.certificate.adapters.excel.processor;

import org.springframework.http.ResponseEntity;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
public interface  ExcelProcessor {
    ResponseEntity<byte[]> makeExcelProcess(String gateway);
}
