package com.cwc.certificate.interview.factory;

import com.cwc.certificate.interview.service.GoogleCalendarService;
import com.cwc.certificate.interview.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/10/31
 * HAPPY DIWALI
 */

@Component
public class InterviewEventFactory {

    private final GoogleCalendarService googleCalendarService;

    private final Map<String, InterviewEventStrategy> strategies;

    @Autowired
    public InterviewEventFactory(GoogleCalendarService googleCalendarService, Map<String, InterviewEventStrategy> strategies) {
        this.googleCalendarService = googleCalendarService;
        this.strategies = strategies;
    }

    public InterviewEventStrategy getStrategy(String action) {
        InterviewEventStrategy strategy = strategies.get(action.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown action: " + action);
        }
        return strategy;
    }

    // TODO: If above Strategy not work then uncomment it and remove @Component(name of bean or mark as @Qualifier --> optional)
    //removed boilerplate code and hardcoded action

//    public InterviewEventStrategy getStrategy(String action) {
//        switch (action.toLowerCase()) {
//            case "create":
//                return new CreateEventStrategy(googleCalendarService);
//            case "cancel":
//                return new CancelEventStrategy(googleCalendarService);
//            case "remove":
//                return new RemoveEventStrategy(googleCalendarService);
//            case "reschedule":
//                return new RescheduleEventStrategy(googleCalendarService);
//            default:
//                throw new IllegalArgumentException("Unknown action: " + action);
//        }
//    }
}

