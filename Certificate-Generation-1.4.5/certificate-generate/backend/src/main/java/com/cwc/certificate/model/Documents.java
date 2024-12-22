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
@Entity
@Builder
@Table(name = "company_documents")
public class Documents implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /**
     TODO : Make it with suitable datastructures and make it fast processing
     */
    private int documentId;
    private String experienceLetterUrl;
    private String relievingLetterUrl;
    private String offerLetterUrl;
    private String salarySlipUrl;
    private String incrementLetterUrl;
    private String apparisalLetterUrl ;
}
