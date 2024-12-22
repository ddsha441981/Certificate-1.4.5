package com.cwc.certificate.utility.print;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EntityPrintService<T> implements PrintService<T> {

    @Override
    public void print(T entity) {
        if (entity == null) {
           log.info("No entity to print.");
            return;
        }
        log.info("Printing entity details:");
        log.info("Entity {}: " ,entity);
    }
}
