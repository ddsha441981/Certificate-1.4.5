package com.cwc.certificate.backfill.repository;

import com.cwc.certificate.backfill.model.BackTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/12/16
 */

@Repository
public interface BackTableRepository extends JpaRepository<BackTable, Long> {
}
