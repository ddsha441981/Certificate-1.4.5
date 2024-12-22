package com.cwc.certificate.pushnotification.controller;

import com.cwc.certificate.pushnotification.service.impl.FirebaseMessagingService;
import com.cwc.certificate.pushnotification.service.PushNotificationService;
import com.cwc.certificate.pushnotification.model.TokenRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/11/01
 */

@RestController
@CrossOrigin("*")
@Slf4j
@RequestMapping("/api/v6/push")
public class PushNotificationTokenController {

    private final PushNotificationService  pushNotificationService;
    private final FirebaseMessagingService firebaseMessagingService;

    @Autowired
    public PushNotificationTokenController(PushNotificationService pushNotificationService, FirebaseMessagingService firebaseMessagingService) {
        this.pushNotificationService = pushNotificationService;
        this.firebaseMessagingService = firebaseMessagingService;
    }

    @PostMapping("/register")
    public void registerUser(@RequestParam String email) {
        pushNotificationService.registerUser(email);
    }

    @PostMapping("/update/device/token")
    public void updateDeviceToken(@RequestBody TokenRequest tokenRequest) {
        pushNotificationService.updateDeviceToken(tokenRequest);
    }

    @PostMapping("/send/notification")
    public void sendNotification(@RequestParam String email, @RequestParam String title, @RequestParam String body) {
        firebaseMessagingService.sendNotification(email, title, body);
    }
}