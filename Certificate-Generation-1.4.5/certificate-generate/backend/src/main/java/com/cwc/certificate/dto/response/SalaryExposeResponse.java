package com.cwc.certificate.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SalaryExposeResponse {
//    private int salaryId;
    private double ctc;
    private double epf;
    private double gratuity;
    private double hra;
    private double profTax;
    private double incomeTax;;
    private double medicalInsurance;
    private double investments80C;
    private double performanceBonus;
    private double grossSalary;
    private double inHandSalary;
    private double monthlyGrossSalary;
    private double monthlyInHandSalary;
}
