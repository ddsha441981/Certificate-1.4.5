package com.cwc.certificate.interview.strategy;

import com.cwc.certificate.interview.model.InterviewEvent;
import com.cwc.certificate.interview.service.GoogleCalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/10/31
 * HAPPY DIWALI
 */
@Component("cancel")
@Slf4j
public class CancelEventStrategy implements InterviewEventStrategy {

    private final GoogleCalendarService googleCalendarService;

    @Autowired
    public CancelEventStrategy(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    @Override
    public String execute(InterviewEvent event) {
        String cancelEvent = googleCalendarService.cancelEvent(event);
        log.info("Cancelling Event for : {}  " , event.getCandidateName());
        return cancelEvent;
    }
}

