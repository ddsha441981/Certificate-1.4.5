package com.cwc.certificate.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DefaultSalaryValuesResponse {
    private int id;
    private double gratuity;
    private double hra;
    private double profTax;
    private double medicalInsurance;
    private double investments80C;
}
