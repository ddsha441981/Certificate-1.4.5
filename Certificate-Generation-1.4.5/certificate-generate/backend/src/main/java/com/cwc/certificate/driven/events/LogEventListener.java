package com.cwc.certificate.driven.events;

import com.cwc.certificate.driven.kafka.LogProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/21
 */

@Service
@Slf4j
public class LogEventListener {

    private final LogProducerService logProducerService;

    public LogEventListener(LogProducerService logProducerService) {
        this.logProducerService = logProducerService;
    }
    @EventListener
    public void handleLogEvent(LogEvent event) {
        log.info("Event triggered for logs to pass kafka consumer:  {} "  , event);
        logProducerService.sendLog(event.getMessage());
    }
}

