package com.cwc.certificate.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
public class CustomLongDeserializer extends JsonDeserializer<Long> {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");

    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        try {
            return DECIMAL_FORMAT.parse(text).longValue();
        } catch (ParseException e) {
            throw new IOException("Error parsing value", e);
        }
    }
}

