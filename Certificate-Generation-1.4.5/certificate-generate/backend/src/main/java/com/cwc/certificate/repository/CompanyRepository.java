package com.cwc.certificate.repository;

import com.cwc.certificate.model.Company;
import com.cwc.certificate.model.enums.ChangeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    List<Company> findByCompanyName(String companyName);
    List<Company> findByChangeStatus(ChangeStatus changeStatus);
    List<Company> findByDeleted(boolean deleted);
    List<Company> findByDeletedAndChangeStatus(boolean deleted, ChangeStatus status);
    List<Company> findByDocumentsCountGreaterThan(int documentCount);

}

