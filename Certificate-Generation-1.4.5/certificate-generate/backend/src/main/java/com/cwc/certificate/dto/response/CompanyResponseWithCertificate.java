package com.cwc.certificate.dto.response;

import com.cwc.certificate.model.Address;
import com.cwc.certificate.model.Documents;
import com.cwc.certificate.model.HrDetails;
import com.cwc.certificate.model.ManagerDetails;
import com.cwc.certificate.model.enums.AddressType;
import com.cwc.certificate.model.enums.ChangeStatus;
import com.cwc.certificate.model.enums.CompanyDomainType;
import com.cwc.certificate.model.enums.CompanyIndustryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompanyResponseWithCertificate {
    private int companyId;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyWebsite;
    private List<Address> addresses;
    private String companyRegistrationNumber;
    private CompanyDomainType companyDomainType;
    private CompanyIndustryType industryType;
    private String yearOfEstablishment;
    private String companySize;
    private String companyFounder;
    private String companyRevenue;
    private String companyLicenseNumber;
    private ManagerDetails managerDetails;
    private AddressType addressType;
    private HrDetails hrDetails;

}
