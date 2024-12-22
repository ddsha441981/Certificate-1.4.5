package com.cwc.certificate.utility.tax;

import com.cwc.certificate.model.TaxSlab;
import com.cwc.certificate.repository.TaxSlabRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TaxCalculator service for calculating tax based on slabs we define tax rate as per our need or company ruless and regulations.
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/08/04
 */
@Service
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class TaxCalculator {

    private final TaxSlabRepository taxSlabRepository;
    private final KieContainer kieContainer;

    private double tax = 0.0;
    private double taxableIncome;
    private String appliedTaxSlab = "None";

    private double taxSlab1Limit = 250000;
    private double taxSlab2Limit = 500000;
    private double taxSlab3Limit = 750000;
    private double taxSlab1Rate = 0.05; // 5%
    private double taxSlab2Rate = 0.1;  // 10%
    private double taxSlab3Rate = 0.2;  // 20%
    private double taxAbove750000Rate = 0.3; // 30%

    @PostConstruct
    private void loadTaxDefaultValues() {
        List<TaxSlab> slabList = taxSlabRepository.findAll();

        if (slabList.isEmpty()) {
            log.warn("No tax slab data found. Using default values.");
        } else {
            log.info("Loading tax slab data from database.");
            slabList.forEach(this::updateTaxSlabs);
        }
    }

    private void updateTaxSlabs(TaxSlab slab) {
        double slabLimit = slab.getSlabLimit();
        double slabRate = slab.getTaxRate();

        if (slabLimit <= taxSlab1Limit) {
            taxSlab1Limit = slabLimit;
            taxSlab1Rate = slabRate;
        } else if (slabLimit <= taxSlab2Limit) {
            taxSlab2Limit = slabLimit;
            taxSlab2Rate = slabRate;
        } else if (slabLimit <= taxSlab3Limit) {
            taxSlab3Limit = slabLimit;
            taxSlab3Rate = slabRate;
        } else {
            taxAbove750000Rate = slabRate;
        }
    }

    public double calculateTax(double taxableIncome) {
        this.taxableIncome = taxableIncome;
        this.appliedTaxSlab = "None";

        KieSession kieSession = null;
        try {
            kieSession = kieContainer.newKieSession("ksession-rules");
            kieSession.setGlobal("taxCalculator", this);
            kieSession.insert(this);
            int firedRules = kieSession.fireAllRules();
            log.info("Number of rules fired: {}", firedRules);
            log.info("Applied Tax Slab: {}", getAppliedTaxSlab());
        } catch (Exception e) {
            log.error("Error calculating tax: {}", e.getMessage());
        } finally {
            if (kieSession != null) {
                kieSession.dispose();
            }
        }

        return tax;
    }
}
