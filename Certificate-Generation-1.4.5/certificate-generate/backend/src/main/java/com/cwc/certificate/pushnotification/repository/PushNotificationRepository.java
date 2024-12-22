package com.cwc.certificate.pushnotification.repository;

import com.cwc.certificate.pushnotification.model.TokenRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PushNotificationRepository extends JpaRepository<TokenRequest, Integer> {

    Optional<TokenRequest> findByEmail(String email);

}
