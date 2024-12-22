package com.cwc.certificate.security.dao.response;


import com.cwc.certificate.security.entities.enums.Role;
import lombok.*;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
