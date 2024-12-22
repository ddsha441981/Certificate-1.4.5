package com.cwc.certificate.utility;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.exceptions.model.GoogleScriptNotVaildException;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@Slf4j
public class URLExtractor {
    public static String extractKey(String fullUrl) {
        try {
            URL url = new URL(fullUrl);
            String path = url.getPath();
            String[] segments = path.split("/");
            for (int i = segments.length - 1; i >= 0; i--) {
                if (ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL.equals(segments[i])) {
                    if (i > 0) {
                        log.info("URL extracted: {}", url);
                        return segments[i - 1];
                    } else {
                        log.error("Google Script Url is empty or not valid url or invalid url format");
                        throw new GoogleScriptNotVaildException("Google Script Url is empty or not valid url or invalid url format");
                    }
                }
            }
            throw new GoogleScriptNotVaildException("Google Script Url is empty or not valid url or invalid url format");
        } catch (Exception e) {
            log.error("Google app script url is not valid or broken : {} " ,e);
            throw new GoogleScriptNotVaildException("Google Script Url is  not valid url");
        }
    }
}


