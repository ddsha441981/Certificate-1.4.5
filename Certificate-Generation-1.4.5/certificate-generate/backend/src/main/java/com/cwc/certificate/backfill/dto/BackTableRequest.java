package com.cwc.certificate.backfill.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/12/16
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BackTableRequest {
    private String entityName;
    private String columnName;
    private Object newValue;
    private int batchSize;
}
