package com.cwc.certificate.driven.kafka;

import com.cwc.certificate.driven.events.LogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/21
 */

@Service
@RequiredArgsConstructor
public class SendLogServiceToConsumer {
    private final ApplicationEventPublisher eventPublisher;
    public void performOperation(String data) {
        eventPublisher.publishEvent(new LogEvent(this, "Performed operation with data: " + data));
    }
}


