package com.cwc.certificate.security.dao.response;

import com.cwc.certificate.security.entities.enums.Gender;
import com.cwc.certificate.security.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private int userId;
    private String token;
    private Role role;
}
