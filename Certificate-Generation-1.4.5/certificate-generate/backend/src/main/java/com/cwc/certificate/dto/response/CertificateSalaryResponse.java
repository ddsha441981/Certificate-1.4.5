package com.cwc.certificate.dto.response;

import com.cwc.certificate.model.Certificate;
import lombok.*;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class CertificateSalaryResponse {
    private Certificate certificate;
    private String salaryFrom;
    private String salaryTo;
    private String salaryMode;
    private String selectedDocumentType;
}
