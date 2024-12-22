package com.cwc.certificate.repository;

import com.cwc.certificate.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {
}
