package com.cwc.certificate.service;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

public interface DocumentService {
    String callGoogleAppsScriptFunctionUsingCache(int certificateId);
    String callGoogleAppsScriptFunction(int certificateId);
}
