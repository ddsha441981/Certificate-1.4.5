package com.cwc.certificate.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@ToString
@Table(name = "manager_details")
public class ManagerDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int managerId;
    private String managerName;
    private String managerContactNumber;
    private String managerEmail;
}
