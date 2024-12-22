package com.cwc.certificate.repository;

import com.cwc.certificate.model.HrDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HrDetailsRepository extends JpaRepository<HrDetails, Integer> {
}
