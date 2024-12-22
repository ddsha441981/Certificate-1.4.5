package com.cwc.certificate.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class HrDetailsRequest {
    private int hrId;
    private String hrName;
    private String hrContactNumber;
    private String hrEmail;

}
