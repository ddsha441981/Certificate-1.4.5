package com.cwc.certificate.utility;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@Service
public class FileValidator {

    private static final String PNG_CONTENT_TYPE = "image/png";

    @SneakyThrows
    public boolean isPngFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && contentType.equals(PNG_CONTENT_TYPE);
    }
}

