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
@Component("remove")
@Slf4j
public class RemoveEventStrategy implements InterviewEventStrategy{
    private final GoogleCalendarService googleCalendarService;

    @Autowired
    public RemoveEventStrategy(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    @Override
    public String execute(InterviewEvent event) {
        String removeEvent = googleCalendarService.removeEvent(event);
        log.info("removing Event for : {}  " , event.getCandidateName());
        return removeEvent;
    }
}
