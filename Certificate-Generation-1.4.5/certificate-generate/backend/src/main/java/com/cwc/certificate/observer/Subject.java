package com.cwc.certificate.observer;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/10
 */
public interface Subject {
     void attach(Observer o);
     void detach(Observer o);
     void notifyUpdate(Message m);
}
