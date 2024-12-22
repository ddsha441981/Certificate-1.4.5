package com.cwc.certificate.interview.service;

import com.cwc.certificate.interview.dto.InterviewEventRequest;
import com.cwc.certificate.interview.model.InterviewEvent;

import java.time.LocalDate;
import java.util.List;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/10/31
 * HAPPY DIWALI
 */
public interface GoogleService extends GoogleServiceCrudOperations{

    InterviewEvent createInterviewAction(InterviewEventRequest eventRequest);
    InterviewEvent cancelInterviewAction(int eventId,String action);
    InterviewEvent removeInterviewAction(int eventId,String action);
    InterviewEvent rescheduleInterviewAction(InterviewEventRequest eventRequest);
    List<InterviewEvent> todayScheduledInterviewList(LocalDate interviewDate);
     long getTodayInterviewsCount();
    List<InterviewEvent> getTodayBookedInterviews();
    long getTodayBookedInterviewsCount();
    List<InterviewEvent> todayCancelledInterviewList();
    long todayCancelledInterviewCount();


}
