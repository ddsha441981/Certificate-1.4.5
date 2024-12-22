package com.cwc.certificate.pushnotification.model;

import lombok.*;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/11/01
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Notification {
    private String title;
    private String body;
}
