package com.cwc.certificate.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

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
@Entity
@Table(name = "scripts")
public class GoogleScript implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String googleId;
    private String scriptUrl;
}
