package com.cwc.certificate.backfill.service;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/12/16
 */
public interface BackTableService {
    public void backfillEntity(String entityName, String columnName, Object newValue, int batchSize);
}
