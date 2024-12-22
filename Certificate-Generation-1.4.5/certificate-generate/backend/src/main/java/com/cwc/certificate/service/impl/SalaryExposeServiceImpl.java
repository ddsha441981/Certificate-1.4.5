package com.cwc.certificate.service.impl;

import com.cwc.certificate.dto.response.SalaryExposeResponse;
import com.cwc.certificate.model.DefaultSalaryValues;
import com.cwc.certificate.model.SalaryExpose;
import com.cwc.certificate.repository.DefaultSalaryValuesRepository;
import com.cwc.certificate.service.SalaryExposeService;
import com.cwc.certificate.utility.tax.TaxCalculator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/06/07
 */

@Service
@Slf4j
public class SalaryExposeServiceImpl implements SalaryExposeService {

    private final DefaultSalaryValuesRepository defaultSalaryValuesRepository;
    private final TaxCalculator taxCalculator;

    @Autowired
    public SalaryExposeServiceImpl(DefaultSalaryValuesRepository defaultSalaryValuesRepository, TaxCalculator taxCalculator) {
        this.defaultSalaryValuesRepository = defaultSalaryValuesRepository;
        this.taxCalculator = taxCalculator;
    }

    private static  double DEFAULT_GRATUITY = 0.0;
    private static  double DEFAULT_HRA = 0.0;
    private static  double DEFAULT_PROF_TAX = 0.0;
    private static  double DEFAULT_MEDICAL_INSURANCE = 0.0;
    private static  double DEFAULT_INVESTMENTS_80C = 0.0;

    @PostConstruct
    private void loadDefaultValues() {
        Optional<DefaultSalaryValues> defaultSalaryValues = this.defaultSalaryValuesRepository.findById(1);
        log.info("Salary value from database {} " , defaultSalaryValues);
        if (defaultSalaryValues.isPresent()){
            DefaultSalaryValues values = defaultSalaryValues.get();
            this.DEFAULT_GRATUITY = values.getGratuity();
            this.DEFAULT_HRA = values.getHra();
            this.DEFAULT_PROF_TAX = values.getProfTax();
            this.DEFAULT_MEDICAL_INSURANCE = values.getMedicalInsurance();
            this.DEFAULT_INVESTMENTS_80C = values.getInvestments80C();
        }else{
            //throw new IllegalStateException("Default salary values not found in the database.");
            log.warn("Default salary values not found in the database. Using fallback values.");
            DefaultSalaryValues values = new DefaultSalaryValues();
            values.setGratuity(DEFAULT_GRATUITY);
            values.setHra(DEFAULT_HRA);
            values.setProfTax(DEFAULT_PROF_TAX);
            values.setMedicalInsurance(DEFAULT_MEDICAL_INSURANCE);
            values.setInvestments80C(DEFAULT_INVESTMENTS_80C);
            log.info("Salary value from default {} " ,values);
        }
    }

    public double calculateGrossSalary(double ctc, Double epf, Double performanceBonus) {
        double epfValue = epf != null ? epf : 0;
        double performanceBonusValue = performanceBonus != null ? performanceBonus : 0;
        return ctc - DEFAULT_GRATUITY - epfValue + performanceBonusValue;
    }

    public double calculateTaxableIncome(double ctc, Double epf, Double performanceBonus) {
        double epfValue = epf != null ? epf : 0;
        double performanceBonusValue = performanceBonus != null ? performanceBonus : 0;
        return ctc - DEFAULT_GRATUITY - epfValue - DEFAULT_INVESTMENTS_80C - DEFAULT_HRA
                - DEFAULT_PROF_TAX - DEFAULT_MEDICAL_INSURANCE + performanceBonusValue;
    }

    public double calculateIncomeTax(double taxableIncome) {
        return taxCalculator.calculateTax(taxableIncome);
    }

    public double calculateInHandSalary(double ctc, Double epf, Double performanceBonus) {
        double grossSalary = calculateGrossSalary(ctc, epf, performanceBonus);
        double taxableIncome = calculateTaxableIncome(ctc, epf, performanceBonus);
        double incomeTax = calculateIncomeTax(taxableIncome);
        return grossSalary - incomeTax - DEFAULT_PROF_TAX - DEFAULT_MEDICAL_INSURANCE;
    }

    public double calculateMonthlyGrossSalary(double grossSalary) {
        return grossSalary / 12;
    }

    public double calculateMonthlyInHandSalary(double inHandSalary) {
        return inHandSalary / 12;
    }

    @Override
    public SalaryExposeResponse calaculateSalaryUsingCTC(double ctc, double epfs, double performanceBonuss) {

        log.info("Starting salary calculation using CTC: {}, EPF: {}, Performance Bonus: {}",
                ctc, epfs, performanceBonuss);
         double epf = Optional.of(epfs).orElse(0.0);
         double performanceBonus = Optional.of(performanceBonuss).orElse(0.0);

        double grossSalary = calculateGrossSalary(ctc, epf, performanceBonus);
        double taxableIncome = calculateTaxableIncome(ctc, epf, performanceBonus);
        double incomeTax = calculateIncomeTax(taxableIncome);
        double inHandSalary = calculateInHandSalary(ctc, epf, performanceBonus);

        SalaryExpose salaryExpose = SalaryExpose.builder()
                .ctc(ctc)
                .epf(epf)
                .performanceBonus(performanceBonus)
                .gratuity(DEFAULT_GRATUITY)
                .hra(DEFAULT_HRA)
                .profTax(DEFAULT_PROF_TAX)
                .incomeTax(incomeTax)
                .medicalInsurance(DEFAULT_MEDICAL_INSURANCE)
                .investments80C(epf)//Update Investment80C = EPF
                .grossSalary(grossSalary)
                .inHandSalary(inHandSalary)
                .monthlyGrossSalary(calculateMonthlyGrossSalary(grossSalary))
                .monthlyInHandSalary(calculateMonthlyInHandSalary(inHandSalary))
                .build();
        log.info("Salary calculation completed: {}", salaryExpose);
        return mapToSalaryExposeCTC(salaryExpose);
    }

    private SalaryExposeResponse mapToSalaryExposeCTC(SalaryExpose salaryExpose) {
        log.info("Mapping SalaryExpose to SalaryExposeResponse: {}", salaryExpose);
        return SalaryExposeResponse.builder()
                .ctc(salaryExpose.getCtc())
                .epf(salaryExpose.getEpf())
                .performanceBonus(salaryExpose.getPerformanceBonus())
                .gratuity(salaryExpose.getGratuity())
                .hra(salaryExpose.getHra())
                .profTax(salaryExpose.getProfTax())
                .incomeTax(salaryExpose.getIncomeTax())
                .medicalInsurance(salaryExpose.getMedicalInsurance())
                .investments80C(salaryExpose.getInvestments80C())
                .grossSalary(salaryExpose.getGrossSalary())
                .inHandSalary(salaryExpose.getInHandSalary())
                .monthlyGrossSalary(calculateMonthlyGrossSalary(salaryExpose.getGrossSalary()))
                .monthlyInHandSalary(calculateMonthlyInHandSalary(salaryExpose.getInHandSalary()))
                .build();
    }

}
