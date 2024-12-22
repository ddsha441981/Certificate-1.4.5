package com.cwc.certificate.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ManagerDetailsRequest {
    private int managerId;
    private String managerName;
    private String managerContactNumber;
    private String managerEmail;
}
