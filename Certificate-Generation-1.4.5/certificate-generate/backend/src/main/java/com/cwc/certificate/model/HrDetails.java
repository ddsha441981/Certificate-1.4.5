package com.cwc.certificate.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Entity
@Table(name = "hr_details")
public class HrDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hrId;
    private String hrName;
    private String hrContactNumber;
    private String hrEmail;
}
