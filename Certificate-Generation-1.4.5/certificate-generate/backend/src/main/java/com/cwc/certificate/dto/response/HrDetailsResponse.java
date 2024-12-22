package com.cwc.certificate.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class HrDetailsResponse {
    private int hrId;
    private String hrName;
    private String hrContactNumber;
    private String hrEmail;
}
