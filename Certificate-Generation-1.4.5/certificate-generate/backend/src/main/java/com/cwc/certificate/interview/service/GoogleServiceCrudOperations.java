package com.cwc.certificate.interview.service;

import com.cwc.certificate.interview.dto.InterviewEventPaginationResponse;
import com.cwc.certificate.interview.model.InterviewEvent;

import java.util.List;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/10/31
 * HAPPY DIWALI
 */
public interface GoogleServiceCrudOperations {
      List<InterviewEventPaginationResponse> findAllInterviewScheduledList(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
     void deleteInterviewByEventId(int eventId);
    InterviewEvent findByEventId(int eventId);
     List<InterviewEvent> findScheduledList(String status);
     List<InterviewEvent> findCancelList(String status);
     List<InterviewEvent> findRecheduledList(String status);

}
