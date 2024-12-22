package com.cwc.certificate.pushnotification.service;

import com.cwc.certificate.pushnotification.model.TokenRequest;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/11/01
 */
public interface PushNotificationService {

      void registerUser(String email);
      void updateDeviceToken(TokenRequest tokenRequest);
     String getDeviceTokenByEmail(String email);
}
