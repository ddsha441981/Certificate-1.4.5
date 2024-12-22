package com.cwc.certificate.nullhandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultNullHandler<T> implements NullHandler<T> {

    @Override
    public T handleNull() {
        log.info("Warning: No specific null handler registered. Returning null.");
        return null;
    }
}

