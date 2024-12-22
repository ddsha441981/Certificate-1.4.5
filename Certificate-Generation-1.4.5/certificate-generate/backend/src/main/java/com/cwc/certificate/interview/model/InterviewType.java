package com.cwc.certificate.interview.model;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/10/31
 * HAPPY DIWALI
 */
public enum InterviewType {
    HR("Human Resources Interview"),
    TECHNICAL("Technical Interview"),
    MANAGERIAL("Managerial Interview"),
    BEHAVIORAL("Behavioral Interview"),
    FINAL("Final Round Interview"),
    TELEPHONIC("Telephonic Interview"),
    GROUP("Group Discussion"),
    CASE_STUDY("Case Study"),
    PANEL("Panel Interview"),
    CODING("Coding Interview"),
    ASSIGNMENT("Assignment-based Interview"),
    MOCK("Mock Interview"),
    PROBLEM_SOLVING("Problem Solving Interview");

    private final String description;

    InterviewType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

