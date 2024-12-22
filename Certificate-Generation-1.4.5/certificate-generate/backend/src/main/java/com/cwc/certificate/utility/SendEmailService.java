package com.cwc.certificate.utility;

import com.cwc.certificate.dto.response.EmailResponse;
import com.cwc.certificate.model.EmailDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/03/13
 */

@Service
@Slf4j
public class SendEmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public SendEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public EmailResponse sendSimpleMail(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());
            javaMailSender.send(mailMessage);
            return new EmailResponse("Mail Sent Successfully...");
        } catch (MailAuthenticationException e) {
            return new EmailResponse("Error: Authentication failed while sending mail.");
        } catch (MailException e) {
            return new EmailResponse("Error: An unexpected error occurred while sending mail.");
        } catch (Exception e) {
            return new EmailResponse("Error: An unexpected error occurred.");
        }
    }
}
