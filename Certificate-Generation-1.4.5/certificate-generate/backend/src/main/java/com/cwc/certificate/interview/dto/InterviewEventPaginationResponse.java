package com.cwc.certificate.interview.dto;

import com.cwc.certificate.interview.model.InterviewEvent;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewEventPaginationResponse {
    private List<InterviewEvent> interviewEventList;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;
}
