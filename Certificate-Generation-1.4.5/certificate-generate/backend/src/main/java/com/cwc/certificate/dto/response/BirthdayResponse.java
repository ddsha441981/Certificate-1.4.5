package com.cwc.certificate.dto.response;

import com.cwc.certificate.security.entities.enums.Gender;
import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class BirthdayResponse {
    private String candidateName;
    private LocalDate dob;
    private String candidateEmail;
    private Gender gender;
}
