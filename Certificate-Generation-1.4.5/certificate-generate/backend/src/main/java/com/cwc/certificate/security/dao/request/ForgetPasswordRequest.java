package com.cwc.certificate.security.dao.request;

import lombok.*;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordRequest {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String changePasswordUrl;
}
