package com.cwc.certificate.model;

import com.cwc.certificate.model.enums.AddressType;
import com.cwc.certificate.model.enums.ChangeStatus;
import com.cwc.certificate.model.enums.CompanyDomainType;
import com.cwc.certificate.model.enums.CompanyIndustryType;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
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
@Transactional
@Builder
@Entity
@ToString(exclude = {"logoData", "signatureData"})

@Table(name = "company")
@EntityListeners(AuditingEntityListener.class)
public class Company implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private int companyId;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyAddress;
    private String companyWebsite;
    private String companyLogo;
    private String imageSignatureType;
    private String imageLogoType;
    @Enumerated(EnumType.STRING)
    private ChangeStatus status;
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    @Lob
    private String logoData;
    private String signatureAuthorities;
    @Lob
    private String signatureData;
    @Column(name = "documents_count", nullable = false, columnDefinition = "integer default 0")
    private int documentsCount;
    @Column(name = "is_deleted")
    private boolean deleted = false;
    @Enumerated(EnumType.STRING)
    private ChangeStatus changeStatus;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;
    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_company_id",referencedColumnName = "company_id")
    private List<Documents> documents;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_company_id",referencedColumnName = "company_id")
    private List<Address> addresses;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "hr_id")
    private HrDetails hrDetails;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "manager_id")
    private ManagerDetails managerDetails;
    private String companyRegistrationNumber;
    @Enumerated(EnumType.STRING)
    private CompanyDomainType companyDomainType;
    @Enumerated(EnumType.STRING)
    private CompanyIndustryType industryType;
    private String yearOfEstablishment;
    private String companySize;
    private String companyFounder;
    private String companyRevenue;
    private String companyLicenseNumber;
}
