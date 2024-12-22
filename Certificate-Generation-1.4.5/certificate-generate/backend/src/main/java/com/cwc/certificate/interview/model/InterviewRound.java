package com.cwc.certificate.interview.model;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/10/31
 * HAPPY DIWALI
 */
public enum InterviewRound {
    PHONE_SCREENING("Phone Screening Round"),
    TECHNICAL_ROUND_1("Technical Round 1"),
    TECHNICAL_ROUND_2("Technical Round 2"),
    TECHNICAL_ROUND_3("Technical Round 3"),
    TECHNICAL_ROUND_4("Technical Round 4"),
    TECHNICAL_ROUND_5("Technical Round 5"),
    MANAGERIAL_ROUND("Managerial Round"),
    HR_ROUND("HR Round"),
    FINAL_ROUND("Final Round"),
    OFFER_STAGE("Offer Stage"),
    REJECTION("Rejection Round");

    private final String description;

    InterviewRound(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

