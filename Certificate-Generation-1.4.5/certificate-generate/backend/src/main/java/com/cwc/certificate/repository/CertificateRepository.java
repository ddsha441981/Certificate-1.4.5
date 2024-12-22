package com.cwc.certificate.repository;

import com.cwc.certificate.model.Certificate;
import com.cwc.certificate.model.enums.ChangeStatus;
import com.cwc.certificate.security.entities.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/02/14
 */
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
    boolean existsByCandidateEmail(String candidateEmail);
    List<Certificate> findByCandidateName(String candidateName);
    @Query("SELECT u.candidateName, u.candidateEmail,u.gender FROM Certificate u WHERE MONTH(u.dob) = :month AND DAY(u.dob) = :day")
    List<Object[]> findCandidateDetailsByDobMonthAndDobDay(@Param("month") int month, @Param("day") int day);
    List<Certificate> findByCompanyName(String companyName);
    List<Certificate> findByChangeStatus(ChangeStatus changeStatus);
    List<Certificate> findByDeleted(boolean deleted);
}
