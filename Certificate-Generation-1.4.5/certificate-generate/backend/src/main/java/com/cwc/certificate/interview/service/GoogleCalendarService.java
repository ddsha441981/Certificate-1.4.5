package com.cwc.certificate.interview.service;

import com.cwc.certificate.interview.model.InterviewEvent;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/10/31
 * HAPPY DIWALI
 */
public interface GoogleCalendarService {
    String createEvent(InterviewEvent event);
    String  cancelEvent(InterviewEvent event);
    String removeEvent(InterviewEvent event);
    String rescheduleEvent(InterviewEvent event);
}
