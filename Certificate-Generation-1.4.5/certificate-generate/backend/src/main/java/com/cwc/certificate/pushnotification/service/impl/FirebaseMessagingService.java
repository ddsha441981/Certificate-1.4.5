package com.cwc.certificate.pushnotification.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author  Deendayal Kumawat
 * @version 1.4.3
 * @since   2024/11/01
 */

@Service
@Slf4j
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    @Autowired
    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public void sendNotification(String deviceToken, String title, String body) {
        if (!isValidDeviceToken(deviceToken)) {
            log.error("Invalid device token: {}", deviceToken);
            throw new IllegalArgumentException("Invalid device token");
        }

        if (!isValidMessageTitle(title)) {
            log.error("Invalid notification title: {}", title);
            throw new IllegalArgumentException("Invalid notification title");
        }

        if (!isValidMessageBody(body)) {
            log.error("Invalid notification body: {}", body);
            throw new IllegalArgumentException("Invalid notification body");
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(notification)
                .build();

        try {
            String response = firebaseMessaging.send(message);
            log.info("Notification sent successfully. Response: {}", response);
        } catch (Exception e) {
            log.error("Error sending notification to token {}: ", deviceToken, e);
        }
    }

    private boolean isValidDeviceToken(String token) {
        return token != null && !token.trim().isEmpty() && token.length() >= 20;
    }

    private boolean isValidMessageTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        return title.length() <= 40;
    }

    private boolean isValidMessageBody(String body) {
        if (body == null || body.trim().isEmpty()) {
            return false;
        }
        return body.length() <= 240;
    }
}
