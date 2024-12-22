package com.cwc.certificate.dto.request;

import com.cwc.certificate.model.Address;
import com.cwc.certificate.model.Documents;
import com.cwc.certificate.model.HrDetails;
import com.cwc.certificate.model.ManagerDetails;
import com.cwc.certificate.model.enums.AddressType;
import com.cwc.certificate.model.enums.ChangeStatus;
import com.cwc.certificate.model.enums.CompanyDomainType;
import com.cwc.certificate.model.enums.CompanyIndustryType;
import lombok.*;

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
public class CompanyRequest {
    private int companyId;
    private String companyName;
    private ChangeStatus changeStatus;
    private String companyEmail;
    private String companyPhone;
    private String companyWebsite;
    private String companyLogo;
    private int documentsCount;
    private String signatureAuthorities;
    private String imageSignatureType;
    private String imageLogoType;
//    private byte[] logoData;
//    private byte[] signatureData;
    private String logoData;
    private String signatureData;
    private boolean deleted = false;
    private ChangeStatus status;
    private List<Address> addresses;
    private AddressType addressType;
    private List<Documents> documents;
    private String companyRegistrationNumber;
    private CompanyDomainType companyDomainType;
    private CompanyIndustryType industryType;
    private String yearOfEstablishment;
    private String companySize;
    private String companyFounder;
    private String companyRevenue;
    private String companyLicenseNumber;

    private ManagerDetails managerDetails;
    private HrDetails hrDetails;


}
