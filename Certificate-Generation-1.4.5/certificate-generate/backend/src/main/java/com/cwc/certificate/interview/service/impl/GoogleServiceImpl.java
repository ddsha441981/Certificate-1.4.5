package com.cwc.certificate.interview.service.impl;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.exceptions.model.EventNotFoundException;
import com.cwc.certificate.exceptions.model.SlotUnavailableException;
import com.cwc.certificate.interview.dto.InterviewEventPaginationResponse;
import com.cwc.certificate.interview.dto.InterviewEventRequest;
import com.cwc.certificate.interview.factory.InterviewEventFactory;
import com.cwc.certificate.interview.model.InterviewEvent;
import com.cwc.certificate.interview.repository.GoogleServiceRepository;
import com.cwc.certificate.interview.service.GoogleService;
import com.cwc.certificate.interview.strategy.InterviewEventContext;
import com.cwc.certificate.interview.strategy.InterviewEventStrategy;
import com.cwc.certificate.pushnotification.service.PushNotificationService;
import com.cwc.certificate.pushnotification.service.impl.FirebaseMessagingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/10/31
 * HAPPY DIWALI
 */

@Service
@Slf4j
public class GoogleServiceImpl implements GoogleService {

    private final GoogleServiceRepository googleServiceRepository;
    private final InterviewEventFactory interviewEventFactory;
    private final Map<String, InterviewEventStrategy> strategies;
    private final PushNotificationService pushNotificationService;
    private final FirebaseMessagingService firebaseMessagingService;

    @Autowired
    public GoogleServiceImpl(GoogleServiceRepository googleServiceRepository, InterviewEventFactory interviewEventFactory, Map<String, InterviewEventStrategy> strategies, PushNotificationService pushNotificationService, FirebaseMessagingService firebaseMessagingService) {
        this.googleServiceRepository = googleServiceRepository;
        this.interviewEventFactory = interviewEventFactory;
        this.strategies = strategies;
        this.pushNotificationService = pushNotificationService;
        this.firebaseMessagingService = firebaseMessagingService;
    }


    @Override
    public InterviewEvent createInterviewAction(InterviewEventRequest eventRequest) {
        validateEventRequest(eventRequest);

        InterviewEvent event = buildInterviewEvent(eventRequest);
        event.setStatus("SCHEDULED");

        log.info("Checking slot availability for the date: {}, Start time: {}, End time: {}", event.getInterviewDate(), event.getStartTime(), event.getEndTime());

        List<InterviewEvent> existingEvents = checkSlotAvailability(event);

        // Book the slot
        log.info("Slot is available. Proceeding to book the slot for the interview.");
        event.setSlot("BOOKED");
        googleServiceRepository.save(event);
        sendBookingNotification(event);

        notifyForRebookedSlots(existingEvents, event);
        String calledStrategy = callStrategy(event, event.getAction());

        String googleEventId = extractGoogleEventId(calledStrategy);
        if (googleEventId != null) {
            event.setGoogleEventId(googleEventId);
            googleServiceRepository.save(event);
        }
        return event;
    }

    private String extractGoogleEventId(String calledStrategy) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(calledStrategy);
            JsonNode eventDataNode = rootNode.path("eventData");
            return eventDataNode.path("googleEventId").asText(null);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Failed to parse JSON response: {}", e.getMessage());
            return null;
        }
    }

    private void validateEventRequest(InterviewEventRequest eventRequest) {
        if (eventRequest == null || eventRequest.getInterviewDate() == null || eventRequest.getStartTime() == null || eventRequest.getEndTime() == null) {
            throw new IllegalArgumentException("Invalid event request. Ensure all required fields are provided.");
        }
    }

    private InterviewEvent buildInterviewEvent(InterviewEventRequest eventRequest) {
        return InterviewEvent.builder()
                .interviewDate(eventRequest.getInterviewDate())
                .startTime(eventRequest.getStartTime())
                .endTime(eventRequest.getEndTime())
                .companyName(eventRequest.getCompanyName())
                .interviewRound(eventRequest.getInterviewRound())
                .interviewType(eventRequest.getInterviewType())
                .candidateName(eventRequest.getCandidateName())
                .location(eventRequest.getLocation())
                .action(eventRequest.getAction())
                .timeZone(eventRequest.getTimeZone())
                .slot(eventRequest.getSlot())
                .email(eventRequest.getEmail())
                .build();
    }

    private List<InterviewEvent> checkSlotAvailability(InterviewEvent event) {
        List<InterviewEvent> existingEvents = googleServiceRepository.findByInterviewDateAndStartTimeBetween(event.getInterviewDate(), event.getStartTime(), event.getEndTime());
        log.info("Existing events: {}", existingEvents);
        boolean slotAvailable = existingEvents.stream().noneMatch(e -> "SCHEDULED".equalsIgnoreCase(e.getStatus()) && "BOOKED".equalsIgnoreCase(e.getSlot()));
        if (!slotAvailable) {
            log.error("No available slot for the selected time range: {} - {}", event.getStartTime(), event.getEndTime());
            throw new SlotUnavailableException("No available slot for the selected time range.");
        }
        return existingEvents;
    }

    private void sendBookingNotification(InterviewEvent event) {
        String deviceToken = pushNotificationService.getDeviceTokenByEmail(event.getEmail());
        if (deviceToken != null && !deviceToken.isEmpty()) {
            String message = String.format("Hey %s, your interview with %s has been Booked.", event.getCandidateName(), event.getCompanyName());
            firebaseMessagingService.sendNotification(deviceToken, "Book Interview", message);
            log.info("Booking notification sent successfully for candidate: {} with company: {}", event.getCandidateName(), event.getCompanyName());
        }
    }

    private void notifyForRebookedSlots(List<InterviewEvent> existingEvents, InterviewEvent event) {
        boolean rebookedSlotFound = existingEvents.stream().anyMatch(e -> "CANCEL".equalsIgnoreCase(e.getStatus()) || "REMOVE".equalsIgnoreCase(e.getStatus()));

        if (rebookedSlotFound) {
            String deviceToken = pushNotificationService.getDeviceTokenByEmail(event.getEmail());
            if (deviceToken != null && !deviceToken.isEmpty()) {
                firebaseMessagingService.sendNotification(deviceToken, "Hello", String.format("Notification triggered: Slot %s is now available.", event.getSlot()));
                log.info("Rebooking notification triggered for candidate: {} in slot: {}", event.getCandidateName(), event.getSlot());
            }
        }
    }

    @Override
    public InterviewEvent cancelInterviewAction(int eventId, String action) {
        InterviewEvent interviewEvent = this.googleServiceRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with id this event Id"));
        // Notify
        String deviceToken = pushNotificationService.getDeviceTokenByEmail(interviewEvent.getEmail());
        if (deviceToken != null && !deviceToken.isEmpty()) {
            //firebase notification event trigger
            this.firebaseMessagingService.sendNotification(deviceToken, "Cancel Interview", "Hey " + interviewEvent.getCandidateName() + ", your interview with " + interviewEvent.getCompanyName() + " has been canceled.");
            log.info("Cancel firebase notification triggered successfully: ");
            log.info("Hey " + interviewEvent.getCandidateName() + ", your interview with " + interviewEvent.getCompanyName() + " has been canceled.");
        }
        interviewEvent.setStatus("CANCELED");
        interviewEvent.setSlot("AVAILABLE");
        this.googleServiceRepository.save(interviewEvent);
        callStrategy(interviewEvent, action);
        return interviewEvent;
    }

    @Override
    public InterviewEvent removeInterviewAction(int eventId, String action) {
        InterviewEvent interviewEventRemove = this.googleServiceRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with id this event Id"));

        // Notify
        String deviceToken = pushNotificationService.getDeviceTokenByEmail(interviewEventRemove.getEmail());
        if (deviceToken != null && !deviceToken.isEmpty()) {
            //firebase notification event trigger
            this.firebaseMessagingService.sendNotification(deviceToken, "Remove Interview", "Hey " + interviewEventRemove.getCandidateName() + ", your interview with " + interviewEventRemove.getCompanyName() + " has been removed.");
            log.info("Removed firebase notification triggered successfully: ");
            log.info("Hey " + interviewEventRemove.getCandidateName() + ", your interview with " + interviewEventRemove.getCompanyName() + " has been removed.");
        }
        interviewEventRemove.setStatus("REMOVED");
        interviewEventRemove.setSlot("AVAILABLE");
        this.googleServiceRepository.save(interviewEventRemove);
        callStrategy(interviewEventRemove, action);
        return interviewEventRemove;
    }

    @Override
    public InterviewEvent rescheduleInterviewAction(InterviewEventRequest eventRequest) {
        int eventId = eventRequest.getEventId();
        ;//current eventId
        String action = eventRequest.getAction();//current action
        InterviewEvent interviewEventReschedule = this.googleServiceRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with id this event Id"));
        interviewEventReschedule.setInterviewDate(eventRequest.getInterviewDate());
        interviewEventReschedule.setStartTime(eventRequest.getStartTime());
        interviewEventReschedule.setEndTime(eventRequest.getEndTime());
        interviewEventReschedule.setAction(action);
        interviewEventReschedule.setStatus("RESCHEDULED");
        interviewEventReschedule.setSlot("BOOKED");
        this.googleServiceRepository.save(interviewEventReschedule);

        // Notify
        String deviceToken = pushNotificationService.getDeviceTokenByEmail(interviewEventReschedule.getEmail());
        if (deviceToken != null && !deviceToken.isEmpty()) {
            //firebase notification event trigger
            this.firebaseMessagingService.sendNotification(deviceToken, "Rescheduled Interview", "Hey " + interviewEventReschedule.getCandidateName() + ", your interview with " + interviewEventReschedule.getCompanyName() + " has been rescheduled.");
            log.info("Rescheduled firebase notification triggered successfully: ");
            log.info("Hey " + interviewEventReschedule.getCandidateName() + ", your interview with " + interviewEventReschedule.getCompanyName() + " has been Rescheduled.");
        }
        String googleEventId = interviewEventReschedule.getGoogleEventId();

        String calledStrategy = callStrategy(interviewEventReschedule, action);
        //update this googleEventId
        String updateGoogleEventId = extractGoogleEventId(calledStrategy);
        if (updateGoogleEventId != null) {
            interviewEventReschedule.setGoogleEventId(googleEventId);
            googleServiceRepository.save(interviewEventReschedule);
        }
        return interviewEventReschedule;
    }

    @Override
    public List<InterviewEvent> todayScheduledInterviewList(LocalDate interviewDate) {
        List<InterviewEvent> interviewEventList = this.googleServiceRepository.findByInterviewDate(interviewDate);
        return interviewEventList;
    }

    @Override
    public long getTodayInterviewsCount() {
        LocalDate localDate = LocalDate.now();
        return this.googleServiceRepository.countByInterviewDate(localDate);
    }

    @Override
    public List<InterviewEvent> getTodayBookedInterviews() {
        LocalDate currentDate = LocalDate.now();
        String status = "BOOKED";
        return googleServiceRepository.findByInterviewDateAndSlot(currentDate, status);
    }

    @Override
    public long getTodayBookedInterviewsCount() {
        LocalDate currentDate = LocalDate.now();
        String status = "BOOKED";
        return googleServiceRepository.countByInterviewDateAndSlot(currentDate, status);
    }

    @Override
    public List<InterviewEvent> todayCancelledInterviewList() {
        LocalDate currentDate = LocalDate.now();
        String status = "CANCELED";
        List<InterviewEvent> andSlot = googleServiceRepository.findByInterviewDateAndStatus(currentDate, status);
        return andSlot;
    }

    @Override
    public long todayCancelledInterviewCount() {
        LocalDate currentDate = LocalDate.now();
        String status = "CANCELED";
        return googleServiceRepository.countByInterviewDateAndStatus(currentDate, status);
    }

    private String callStrategy(InterviewEvent event, String action) {
        // select your strategy no need to select manully selected at run time
        InterviewEventContext context = new InterviewEventContext();
        InterviewEventStrategy strategy = interviewEventFactory.getStrategy(action);
        context.setStrategy(strategy);
        String executed = context.executeStrategy(event);
        return executed;
    }


    @Override
    public List<InterviewEventPaginationResponse> findAllInterviewScheduledList(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "interviewDate";
        }
        Sort sort = (sortDir.equalsIgnoreCase(ConstantValue.DEFAULT_SORT_DIR)) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pages = PageRequest.of(pageNumber, pageSize, sort);

        Page<InterviewEvent> interviewList = this.googleServiceRepository.findAll(pages);
        List<InterviewEventPaginationResponse> responses = new ArrayList<>();
        do {
            List<InterviewEvent> interviewListContent = interviewList.getContent();
            InterviewEventPaginationResponse eventPaginationResponse = InterviewEventPaginationResponse.builder().interviewEventList(interviewListContent).pageNumber(interviewList.getNumber()).pageSize(interviewList.getSize()).totalElements(interviewList.getTotalElements()).totalPages(interviewList.getTotalPages()).lastPage(interviewList.isLast()).build();

            responses.add(eventPaginationResponse);
            if (!interviewList.isLast()) {
                pageNumber++;
                pages = PageRequest.of(pageNumber, pageSize, sort);
                interviewList = this.googleServiceRepository.findAll(pages);
            }
        } while (!interviewList.isLast());

        return responses;
    }

    @Override
    public void deleteInterviewByEventId(int eventId) {
        this.googleServiceRepository.deleteById(eventId);
    }

    @Override
    public InterviewEvent findByEventId(int eventId) {
        return this.googleServiceRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with this event id"));
    }

    @Override
    public List<InterviewEvent> findScheduledList(String status) {
        List<InterviewEvent> eventList = this.googleServiceRepository.findByStatus(status);
        return eventList;
    }

    @Override
    public List<InterviewEvent> findCancelList(String status) {
        List<InterviewEvent> eventList = this.googleServiceRepository.findByStatus(status);
        return eventList;
    }

    @Override
    public List<InterviewEvent> findRecheduledList(String status) {
        List<InterviewEvent> eventList = this.googleServiceRepository.findByStatus(status);
        return eventList;
    }
}
