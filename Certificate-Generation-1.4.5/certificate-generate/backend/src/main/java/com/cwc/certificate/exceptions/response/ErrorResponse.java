package com.cwc.certificate.exceptions.response;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import lombok.*;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ErrorResponse {
    private  UUID errorID;
    private int statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;
    private String path;
}