package com.cwc.certificate.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.DecimalFormat;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
public class CustomLongSerializer extends JsonSerializer<Long> {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeString(DECIMAL_FORMAT.format(value));
        }
    }
}
