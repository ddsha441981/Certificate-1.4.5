package com.cwc.certificate.interview.strategy;

import com.cwc.certificate.interview.model.InterviewEvent;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/10/31
 * HAPPY DIWALI
 */
public class InterviewEventContext {
    private InterviewEventStrategy strategy;

    public void setStrategy(InterviewEventStrategy strategy) {
        this.strategy = strategy;
    }
    public String executeStrategy(InterviewEvent event) {
        String execute = strategy.execute(event);
        return execute;
    }
}

