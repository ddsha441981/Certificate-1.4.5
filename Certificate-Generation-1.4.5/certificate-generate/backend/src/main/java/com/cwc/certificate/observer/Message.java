package com.cwc.certificate.observer;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/10
 */

public class Message {
    final String messageContent;

    public Message (String m) {
        this.messageContent = m;
    }

    public String getMessageContent() {
        return messageContent;
    }
}
