package com.cwc.certificate.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/06/07
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Embeddable
@ToString
public class SalaryExpose implements Serializable {
    private double ctc;
    private double epf;
    /**
     TODO: need to calculate gratuity of employee
     */
    private double gratuity;
    private double hra;
    private double profTax;
    private double incomeTax;
    private double medicalInsurance;
    private double investments80C;
    private double performanceBonus;
    private double grossSalary;
    private double inHandSalary;
    private double monthlyGrossSalary;
    private double monthlyInHandSalary;
}
