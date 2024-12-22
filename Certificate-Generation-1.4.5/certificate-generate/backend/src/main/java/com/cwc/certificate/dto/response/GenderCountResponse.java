package com.cwc.certificate.dto.response;

import com.cwc.certificate.security.entities.enums.Gender;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class GenderCountResponse {
    private Gender gender;
    private Long count;
}
