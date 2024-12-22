package com.cwc.certificate.driven.rabbitmq;

import com.cwc.certificate.dto.response.MailResponse;
import com.cwc.certificate.model.EmailDetails;
import com.cwc.certificate.utility.SendEmailService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/26
 */

@Service
@Slf4j
public class MessageListenerService {

    private final RabbitTemplate rabbitTemplate;

    private final SendEmailService sendEmailService;

    @Autowired
    public MessageListenerService(RabbitTemplate rabbitTemplate, SendEmailService sendEmailService) {
        this.rabbitTemplate = rabbitTemplate;
        this.sendEmailService = sendEmailService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void listen(EmailDetails emailDetails, Channel channel) throws IOException {
        // TODO: Manually trigger message retry if processing fails (Channel usage skipped in this code)
        String replyTo = "No ReplyTo address";
        String correlationId = "No Correlation Id";

        if (emailDetails != null) {
            replyTo = emailDetails.getRecipient();
            correlationId = emailDetails.getSubject();

            sendEmailService.sendSimpleMail(emailDetails);
//            log.info("Email sent successfully to: " + replyTo);
        }
        MailResponse responseMessage = new MailResponse();
        responseMessage.setRecipient(replyTo);
        responseMessage.setStatus(true);
        responseMessage.setResponseMessage("Mail Sent Successfully...");

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, responseMessage);
        //log.info("Mail triggered successfully on : {} " , responseMessage.getRecipient());
    }


}

