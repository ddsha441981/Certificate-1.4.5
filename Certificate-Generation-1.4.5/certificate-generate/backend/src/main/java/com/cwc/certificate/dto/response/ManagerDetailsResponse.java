package com.cwc.certificate.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ManagerDetailsResponse {
    private int managerId;
    private String managerName;
    private String managerContactNumber;
    private String managerEmail;
}
