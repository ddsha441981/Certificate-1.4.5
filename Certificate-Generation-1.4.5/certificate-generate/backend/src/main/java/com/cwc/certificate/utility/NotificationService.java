package com.cwc.certificate.utility;

import com.cwc.certificate.model.EmailDetails;
import com.cwc.certificate.driven.rabbitmq.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final RabbitTemplate rabbitTemplate;

    private final SendEmailService sendEmailService;

    public void sendNotification(EmailDetails emailDetails) {
        // Send the message to RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, emailDetails);

        //this.sendEmailService.sendSimpleMail(emailDetails);
        log.info("Birthday notification trigger {} " , emailDetails.getRecipient());
    }
}
