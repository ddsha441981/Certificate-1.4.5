package com.cwc.certificate.repository;

import com.cwc.certificate.model.DefaultSalaryValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultSalaryValuesRepository extends JpaRepository<DefaultSalaryValues,Integer> {

}
