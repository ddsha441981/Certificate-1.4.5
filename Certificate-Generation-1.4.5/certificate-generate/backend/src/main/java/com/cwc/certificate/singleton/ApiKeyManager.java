package com.cwc.certificate.singleton;

import java.util.HashMap;
import java.util.Map;


//Singleton
public enum ApiKeyManager {
    INSTANCE;

    private Map<String, String> apiKeys;
    ApiKeyManager() {
        apiKeys = new HashMap<>();
        apiKeys.put("google", "YOUR_GOOGLE_API_KEY");
        apiKeys.put("aws", "YOUR_AWS_API_KEY");
        apiKeys.put("recaptcha", "YOUR_RECAPTCHA_API_KEY");
        apiKeys.put("firebase", "YOUR_FIREBASE_API_KEY");
        apiKeys.put("sonar", "YOUR_SONAR_API_KEY");
    }
    public String getApiKey(String service) {
        return apiKeys.get(service);
    }
    public void updateApiKey(String service, String apiKey) {
        apiKeys.put(service, apiKey);
    }
}
