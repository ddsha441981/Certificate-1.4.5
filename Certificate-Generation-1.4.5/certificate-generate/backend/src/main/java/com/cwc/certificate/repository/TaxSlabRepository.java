package com.cwc.certificate.repository;

import com.cwc.certificate.model.TaxSlab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxSlabRepository extends JpaRepository<TaxSlab, Integer> {
}
