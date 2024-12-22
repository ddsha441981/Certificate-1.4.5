package com.cwc.certificate.pushnotification.service.impl;

import com.cwc.certificate.pushnotification.repository.PushNotificationRepository;
import com.cwc.certificate.pushnotification.model.TokenRequest;
import com.cwc.certificate.pushnotification.service.PushNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/11/01
 */

@Service
@Slf4j
public class PushNotificationServiceImpl implements PushNotificationService {

    private final PushNotificationRepository pushNotificationRepository;

    @Autowired
    public PushNotificationServiceImpl(PushNotificationRepository pushNotificationRepository) {
        this.pushNotificationRepository = pushNotificationRepository;
    }

    public void registerUser(String email) {
        Optional<TokenRequest> existingUser = pushNotificationRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            pushNotificationRepository.save(new TokenRequest(email, null));
            log.info("User registered with email: " + email);
        } else {
            log.info("User already exists with email: " + email);
        }
    }

    public void updateDeviceToken(TokenRequest tokenRequest) {
        pushNotificationRepository.save(tokenRequest);
        log.info("Device token updated for email: " + tokenRequest.getEmail());
    }

    public String getDeviceTokenByEmail(String email) {
        Optional<TokenRequest> user = pushNotificationRepository.findByEmail(email);
        return user.map(TokenRequest::getToken).orElse(null);
    }
}

