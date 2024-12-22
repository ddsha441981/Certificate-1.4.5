package com.cwc.certificate.driven.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/21
 */
@Service
@Slf4j
public class LogProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.logs}")
    private String TOPIC;

    @Autowired
    public LogProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLog(String logMessage) {
        log.info("Logs Sends to kafka consumer {} : " , logMessage);
        kafkaTemplate.send(TOPIC, logMessage);
    }
}

