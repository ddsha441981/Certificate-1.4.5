package com.cwc.certificate.nullhandler;

public class NullHandlerUtil {
    public static <T> T handleNull(T object, Class<T> clazz) {
        NullHandler<T> handler = NullHandlerRegistry.getInstance().getHandler(clazz);
        if (object == null) {
            return handler.handleNull();
        }
        return object;
    }
}
