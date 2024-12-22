package com.cwc.certificate.utility.scheduler;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.config.msg.BirthdayMsgList;
import com.cwc.certificate.dto.response.BirthdayResponse;
import com.cwc.certificate.model.EmailDetails;
import com.cwc.certificate.security.entities.User;
import com.cwc.certificate.security.service.UserService;
import com.cwc.certificate.service.BirthdayService;
import com.cwc.certificate.utility.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Service
public class BirthdayReminderService {
    private final BirthdayService birthdayService;
    private final NotificationService notificationService;

    @Autowired
    public BirthdayReminderService(BirthdayService birthdayService, NotificationService notificationService) {
        this.birthdayService = birthdayService;
        this.notificationService = notificationService;
    }


    @Scheduled(cron = "0 0 0 * * *") // Run at midnight every day
    public void sendBirthdayNotifications() {
        List<BirthdayResponse> upcomingBirthdays = birthdayService.getBirthdaysForToday();
        // if data found then send email notification
        sendEmailForBirthdayNotification(upcomingBirthdays);
        }

    private void sendEmailForBirthdayNotification(List<BirthdayResponse> upcomingBirthdays) {
        Random random = new Random();
        for (BirthdayResponse user : upcomingBirthdays) {
            String name = user.getCandidateName();

            String birthdayMessage = String.format(BirthdayMsgList.BIRTHDAY_MESSAGES.get(random.nextInt(BirthdayMsgList.BIRTHDAY_MESSAGES.size())), name,name);
            String subject = ConstantValue.BIRTHDAY_SUBJECT;
            String email = user.getCandidateEmail();

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(email)
                    .subject(subject)
                    .msgBody(birthdayMessage)
                    .build();
            notificationService.sendNotification(emailDetails);
        }
    }
}

