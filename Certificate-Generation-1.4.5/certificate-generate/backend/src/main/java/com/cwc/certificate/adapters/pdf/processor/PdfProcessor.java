package com.cwc.certificate.adapters.pdf.processor;

import org.springframework.http.ResponseEntity;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

public interface  PdfProcessor {
    ResponseEntity<byte[]> makePdfProcess(String gateway);
}
