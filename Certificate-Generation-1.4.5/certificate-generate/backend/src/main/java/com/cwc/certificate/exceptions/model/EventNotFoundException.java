package com.cwc.certificate.exceptions.model;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException() {
    }

    public EventNotFoundException(String msg) {
        super(msg);
    }
}
