package com.cwc.certificate.interview.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/10/31
 * HAPPY DIWALI
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Entity
@Table(name = "interview_events")
public class InterviewEvent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate interviewDate;
    private String startTime;
    private String endTime;
    private String companyName;
    private String interviewRound;
    private String interviewType;
    private String candidateName;
    private String location;
    private String timeZone;
    private String action;
    private String status;
    private String slot;
    private String email;
    private String googleEventId;

}
