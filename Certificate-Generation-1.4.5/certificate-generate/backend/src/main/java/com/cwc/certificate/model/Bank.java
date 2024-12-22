package com.cwc.certificate.model;

import com.cwc.certificate.model.enums.ChangeStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "bank")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Bank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bankId;
    private String bankName;
    private String ifscCode;
    private String accountNo;
    private String accountHolderName;
    private String customerId;
    private String uanNumber;
    private String esiNumber;
    @Enumerated(EnumType.STRING)
    private ChangeStatus changeStatus = ChangeStatus.ACTIVE;
    @Column(name = "is_deleted")
    private boolean deleted = false;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;
    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;


}
