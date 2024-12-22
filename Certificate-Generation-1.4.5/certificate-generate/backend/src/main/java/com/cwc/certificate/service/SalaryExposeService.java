package com.cwc.certificate.service;

import com.cwc.certificate.dto.response.SalaryExposeResponse;

public interface SalaryExposeService {
     SalaryExposeResponse calaculateSalaryUsingCTC(double ctc, double epf, double performanceBonus);

}
