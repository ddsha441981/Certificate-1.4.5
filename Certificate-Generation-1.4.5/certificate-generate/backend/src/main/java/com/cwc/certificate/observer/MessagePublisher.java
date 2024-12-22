package com.cwc.certificate.observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/10
 */

@Component
public class MessagePublisher implements Subject{

    List<Observer> observers  = new ArrayList<>();
    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyUpdate(Message m) {
        for (Observer o : observers){
            o.update(m);
        }
    }
}
