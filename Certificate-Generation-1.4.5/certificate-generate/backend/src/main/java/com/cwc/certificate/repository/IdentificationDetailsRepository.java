package com.cwc.certificate.repository;

import com.cwc.certificate.model.IdentificationDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
public interface IdentificationDetailsRepository extends JpaRepository<IdentificationDetails, Integer> {
}
