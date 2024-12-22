package com.cwc.certificate.service;

import com.cwc.certificate.model.GoogleScript;
import com.cwc.certificate.dto.request.GoogleScriptRequest;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
public interface GoogleScriptService {
    GoogleScript updateGoogleScript(GoogleScriptRequest googleScriptRequest,String googleId);
}
