package com.cwc.certificate.nullhandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NullHandlerRegistry {

    private static final Map<Class<?>, NullHandler<?>> handlers = new ConcurrentHashMap<>();
    private static final NullHandlerRegistry INSTANCE = new NullHandlerRegistry();

    private NullHandlerRegistry() {}
    public static NullHandlerRegistry getInstance() {
        return INSTANCE;
    }
    public <T> void registerHandler(Class<T> clazz, NullHandler<T> handler) {
        handlers.put(clazz, handler);
    }

    @SuppressWarnings("unchecked")
    public <T> NullHandler<T> getHandler(Class<T> clazz) {
        return (NullHandler<T>) handlers.getOrDefault(clazz, new DefaultNullHandler<>());
    }
}

