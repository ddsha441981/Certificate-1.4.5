package com.cwc.certificate.dto.response;

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
public class GoogleScriptResponse {
    private String googleId;
    private String scriptUrl;
}
