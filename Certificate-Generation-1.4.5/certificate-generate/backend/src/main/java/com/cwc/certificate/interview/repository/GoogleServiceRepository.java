package com.cwc.certificate.interview.repository;

import com.cwc.certificate.interview.model.InterviewEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/10/31
 * HAPPY DIWALI
 */

@Repository
public interface GoogleServiceRepository extends JpaRepository<InterviewEvent ,Integer> {
    List<InterviewEvent> findByStatus(String status);
    List<InterviewEvent> findByInterviewDateAndStartTimeBetween(LocalDate interviewDate, String startTime, String endTime);
    List<InterviewEvent> findByInterviewDate(LocalDate interviewDate);
    long countByInterviewDate(LocalDate interviewDate);
    List<InterviewEvent> findByInterviewDateAndSlot(LocalDate interviewDate, String status);
    List<InterviewEvent> findByInterviewDateAndStatus(LocalDate interviewDate, String status);
    long countByInterviewDateAndStatus(LocalDate interviewDate, String status);
    long countByInterviewDateAndSlot(LocalDate interviewDate, String status);
}
