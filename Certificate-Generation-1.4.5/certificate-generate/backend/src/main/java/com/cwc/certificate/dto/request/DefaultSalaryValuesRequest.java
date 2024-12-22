package com.cwc.certificate.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DefaultSalaryValuesRequest {
    private int id;
    private double gratuity;
    private double hra;
    private double profTax;
    private double medicalInsurance;
    private double investments80C;
}
