package com.cwc.certificate.backfill.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/12/16
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class BackTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String column1;
    private Integer column2;
    private LocalDateTime updatedAt;
}
