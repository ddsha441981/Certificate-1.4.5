package com.cwc.certificate.driven.events;

import org.springframework.context.ApplicationEvent;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/21
 */
public class LogEvent extends ApplicationEvent {

    private String message;
    public LogEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
