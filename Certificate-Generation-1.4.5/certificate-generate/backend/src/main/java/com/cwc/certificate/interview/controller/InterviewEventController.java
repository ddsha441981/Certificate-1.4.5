package com.cwc.certificate.interview.controller;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.interview.dto.InterviewEventPaginationResponse;
import com.cwc.certificate.interview.dto.InterviewEventRequest;
import com.cwc.certificate.interview.factory.InterviewEventFactory;
import com.cwc.certificate.interview.model.InterviewEvent;
import com.cwc.certificate.interview.service.GoogleCalendarService;
import com.cwc.certificate.interview.service.GoogleService;
import com.cwc.certificate.interview.strategy.InterviewEventStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/10/31
 * HAPPY DIWALI
 */

@RestController
@CrossOrigin("*")
@Slf4j
@RequestMapping("/api/v6/interviews")
public class InterviewEventController {

    private final InterviewEventFactory interviewEventFactory;
    private final Map<String, InterviewEventStrategy> strategies;

    private final GoogleService googleService;

    @Autowired
    public InterviewEventController(GoogleCalendarService googleCalendarService, Map<String, InterviewEventStrategy> strategies, GoogleService googleService) {
        this.googleService = googleService;
        this.interviewEventFactory = new InterviewEventFactory(googleCalendarService,strategies);
        this.strategies = strategies;
    }

    @PostMapping("/schedule/interview")
    public ResponseEntity<?> scheduleInterview(@RequestBody InterviewEventRequest eventRequest) {
        InterviewEvent interviewAction = this.googleService.createInterviewAction(eventRequest);
        return ResponseEntity.ok("Interview " + eventRequest.getAction() + "d successfully" + interviewAction);
    }

    @PutMapping("/reschedule/interview")
    public ResponseEntity<?> reScheduleInterview(@RequestBody InterviewEventRequest eventRequest) {
        InterviewEvent interviewAction = this.googleService.rescheduleInterviewAction(eventRequest);
        return ResponseEntity.ok("Interview " + eventRequest.getAction() + "d successfully" + interviewAction);
    }

    @GetMapping("/cancel/interview/{eventId}/action/{action}")
    public ResponseEntity<?> cancelInterview(@PathVariable ("eventId") int eventId ,@PathVariable("action") String action) {
        InterviewEvent interviewAction = this.googleService.cancelInterviewAction(eventId,action);
        return ResponseEntity.ok("Interview " + interviewAction.getAction() + "d successfully" + interviewAction);
    }

    @GetMapping("/remove/interview/{eventId}/action/{action}")
    public ResponseEntity<?> removeInterview(@PathVariable ("eventId") int eventId,@PathVariable("action") String action) {
        InterviewEvent interviewAction = this.googleService.removeInterviewAction(eventId,action);
        return ResponseEntity.ok("Interview " + interviewAction.getAction() + "d successfully" + interviewAction);
    }

    @GetMapping("/pagination/list")
    public ResponseEntity<Map<String, Object>> getListInterviewSchelduledList(
            @RequestParam(value = "pageNumber", defaultValue = ConstantValue.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = ConstantValue.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "interviewDate", required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = ConstantValue.DEFAULT_SORT_DIR, required = false) String sortOrder
    ){
        List<InterviewEventPaginationResponse> allInterviewScheduledList = this.googleService.findAllInterviewScheduledList(pageNumber, pageSize, sortBy, sortOrder);
        Map<String, Object> response = new HashMap<>();
        response.put("items", allInterviewScheduledList);
        response.put("totalItems", allInterviewScheduledList.size());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Without Status fetch combine list
    @GetMapping("/today/list")
    public List<InterviewEvent> getTodayInterviews(LocalDate localDate) {
        return googleService.todayScheduledInterviewList(localDate);
    }

    //Without Status fetch all list
    @GetMapping("/all/count")
    public long getTodayInterviewsCount() {
        return googleService.getTodayInterviewsCount();
    }

    @GetMapping("/today/booked")
    public ResponseEntity<List<InterviewEvent>> getTodayBookedInterviews() {
        List<InterviewEvent> todayBookedInterviews = this.googleService.getTodayBookedInterviews();
        return new ResponseEntity<>(todayBookedInterviews,HttpStatus.OK);
    }

    @GetMapping("/today/booked/count")
    public long getTodayBookedInterviewsCount() {
        long todayBookedInterviewsCount = this.googleService.getTodayBookedInterviewsCount();
        return todayBookedInterviewsCount;
    }


    @GetMapping("/today/cancel")
    public ResponseEntity<List<InterviewEvent>> getTodayCancelledInterviews() {
        List<InterviewEvent> todayCancelledInterviewListInterviews = this.googleService.todayCancelledInterviewList();
        return new ResponseEntity<>(todayCancelledInterviewListInterviews,HttpStatus.OK);
    }

    @GetMapping("/today/cancel/count")
    public long getTodayCancelledInterviewsCount() {
        long todayCancelledInterviewCount = this.googleService.todayCancelledInterviewCount();
        return todayCancelledInterviewCount;
    }
}


