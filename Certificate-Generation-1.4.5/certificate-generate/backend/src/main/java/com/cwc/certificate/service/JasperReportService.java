package com.cwc.certificate.service;

import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Deprecated
public interface JasperReportService {
    String reports(String reportFormat, Integer certificateId) throws FileNotFoundException, JRException;
//    String experienceLetter(String reportFormat, Integer certificateId);
//    String salarySlip(String reportFormat, Integer certificateId);
}
