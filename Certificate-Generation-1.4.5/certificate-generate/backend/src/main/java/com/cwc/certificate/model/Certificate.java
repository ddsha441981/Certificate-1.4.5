package com.cwc.certificate.model;

import com.cwc.certificate.customannotation.annotations.ValidEmail;
import com.cwc.certificate.dto.response.CompanyResponseWithCertificate;
import com.cwc.certificate.model.enums.ChangeStatus;
import com.cwc.certificate.security.entities.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;
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
@Builder
@Entity
@Table(name = "certificate")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Certificate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int certificateId;
    private String candidateName;
    private String selectedCompany;
    private String offerDate;
    private String dateOfJoining;
    private LocalDate dob;
    private String jobTitle;
    private String employeeCode;
    private String companyName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "count_docs", nullable = false, columnDefinition = "integer default 0")
    private int countDocs = 0;

    @ValidEmail
    @Column(unique=true)
    private String candidateEmail;
    @Enumerated(EnumType.STRING)
    @Column(name = "change_status")
    private ChangeStatus changeStatus;
    private String salaryInWord;
    @Enumerated(EnumType.STRING)
    private ChangeStatus status;
    @Column(name = "is_deleted")
    private boolean deleted = false;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;
    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_id")
    private Bank bank;
    @Embedded
    private SalaryExpose salaryExpose;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "identificationId")
    private IdentificationDetails identificationDetails;
    @ElementCollection
    private List<String> selectedDocumentTypes;

    @Transient
    private CompanyResponseWithCertificate company;
}
