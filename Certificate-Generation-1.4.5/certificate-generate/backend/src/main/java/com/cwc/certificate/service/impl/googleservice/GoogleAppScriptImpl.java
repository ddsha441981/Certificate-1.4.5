package com.cwc.certificate.service.impl.googleservice;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.request.GoogleScriptRequest;
import com.cwc.certificate.exceptions.model.GoogleScriptNotFoundException;
import com.cwc.certificate.model.GoogleScript;
import com.cwc.certificate.repository.GoogleScriptRepository;
import com.cwc.certificate.service.GoogleScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Service
public class GoogleAppScriptImpl implements GoogleScriptService {

    private final GoogleScriptRepository googleScriptRepository;

    @Autowired
    public GoogleAppScriptImpl(GoogleScriptRepository googleScriptRepository) {
        this.googleScriptRepository = googleScriptRepository;
    }

    @Transactional
    @Override
    public GoogleScript updateGoogleScript(GoogleScriptRequest googleScriptRequest, String googleId) {
        validateInputs(googleScriptRequest, googleId);
        Map<String, GoogleScript> googleScriptConfigMap = Map.of(
                ConstantValue.DEFAULT_DOCS_NAME, new GoogleScript(ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_ID_DOCUMENT, ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_VALUE_DOCUMENT),
                ConstantValue.DEFAULT_SALARY_NAME, new GoogleScript(ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_ID_SALARY, ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_VALUE_SALARY),
                ConstantValue.DEFAULT_DRIVE_NAME, new GoogleScript(ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_ID_DRIVE, ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_VALUE_DRIVE),
                ConstantValue.DEFAULT_INTERVIEW_NAME, new GoogleScript(ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_ID_INTERVIEW, ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_VALUE_INTERVIEW)
        );
        GoogleScript config = googleScriptConfigMap.get(googleId);
        if (config == null) {
            throw new IllegalArgumentException("Invalid googleId: " + googleId);
        }
        GoogleScript existingScript = googleScriptRepository.findById(googleId)
                .orElseThrow(() -> new GoogleScriptNotFoundException("No Script Found for this Id: " + googleId));
        existingScript.setScriptUrl(googleScriptRequest.getScriptUrl());
        return googleScriptRepository.save(existingScript);
    }
    private void validateInputs(GoogleScriptRequest googleScriptRequest, String googleId) {
        if (googleScriptRequest == null || googleScriptRequest.getScriptUrl() == null || googleScriptRequest.getScriptUrl().isEmpty()) {
            throw new IllegalArgumentException("GoogleScriptRequest or script URL cannot be null or empty");
        }
        if (googleId == null || googleId.isEmpty()) {
            throw new IllegalArgumentException("googleId cannot be null or empty");
        }
    }

    //Removed boilerplate code if not working properly uncomment it
//    @Override
//    public GoogleScript updateGoogleScript(GoogleScriptRequest googleScriptRequest,String googleId) {
//        String scriptUrl;
//        String scriptId;
//
//        if (ConstantValue.DEFAULT_DOCS_NAME.equals(googleId)) {
//            scriptId = ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_ID_DOCUMENT;
//            scriptUrl = ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_VALUE_DOCUMENT;
//        } else if (ConstantValue.DEFAULT_SALARY_NAME.equals(googleId)) {
//            scriptId = ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_ID_SALARY;
//            scriptUrl = ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_VALUE_SALARY;
//        } else if (ConstantValue.DEFAULT_DRIVE_NAME.equals(googleId)) {
//            scriptId = ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_ID_DRIVE;
//            scriptUrl = ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_VALUE_DRIVE;
//        }else if (ConstantValue.DEFAULT_INTERVIEW_NAME.equals(googleId)) {
//            scriptId = ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_ID_INTERVIEW;
//            scriptUrl = ConstantValue.DEFAULT_APP_SCRIPT_GOOGLE_VALUE_INTERVIEW;
//        } else {
//            throw new IllegalArgumentException("Invalid googleId");
//        }
//        GoogleScript googleScript = new GoogleScript(scriptId,scriptUrl);
//        this.googleScriptRepository.save(googleScript);
//        //before update call constant data and save it in database
//
//        GoogleScript script = googleScriptRepository.findById(googleId).orElseThrow(() -> new RuntimeException("No Script Found for this Id: " + googleId));
//        script.setScriptUrl(googleScriptRequest.getScriptUrl());
//       return  googleScriptRepository.save(script);
//    }
}
