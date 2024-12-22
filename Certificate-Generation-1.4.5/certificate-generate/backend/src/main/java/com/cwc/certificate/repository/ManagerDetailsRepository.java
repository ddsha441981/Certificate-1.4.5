package com.cwc.certificate.repository;

import com.cwc.certificate.model.ManagerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerDetailsRepository extends JpaRepository<ManagerDetails,Integer> {
}
