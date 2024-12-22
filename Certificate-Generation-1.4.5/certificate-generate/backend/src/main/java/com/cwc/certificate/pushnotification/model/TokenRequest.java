package com.cwc.certificate.pushnotification.model;


import jakarta.persistence.*;
import lombok.*;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/11/01
 */

@Entity
@Table(name = "token")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TokenRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int tokenId;
    private String token;
    private String email;

    public TokenRequest(String email, Object o) {
    }
}
