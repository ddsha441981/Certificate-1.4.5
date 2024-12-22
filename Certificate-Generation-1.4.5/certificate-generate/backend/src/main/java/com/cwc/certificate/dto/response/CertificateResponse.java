package com.cwc.certificate.dto.response;

import com.cwc.certificate.model.Bank;
import com.cwc.certificate.model.Company;
import com.cwc.certificate.model.SalaryExpose;
import com.cwc.certificate.model.enums.ChangeStatus;
import com.cwc.certificate.model.IdentificationDetails;
import com.cwc.certificate.security.entities.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
public class CertificateResponse {
    private int certificateId;
    private String candidateName;
    private String selectedCompany;
//    @MyDateFormat

    private String offerDate;
//    @MyDateFormat
    private String dateOfJoining;
    private LocalDate dob;
    private Gender gender;
    private String jobTitle;
    private String employeeCode;
    private String companyName;
    private String candidateEmail;
    private ChangeStatus changeStatus;
    private String salaryInWord;
    private ChangeStatus status;
    private int countDocs;
    private boolean deleted = false;
    private Bank bank;
    private IdentificationDetails identificationDetails;
    private List<String> selectedDocumentTypes;
    private SalaryExpose salaryExpose;
    private CompanyResponseWithCertificate company;

    public CertificateResponse(int countDocs, String companyName) {
        this.countDocs = countDocs;
        this.companyName = companyName;
    }
}