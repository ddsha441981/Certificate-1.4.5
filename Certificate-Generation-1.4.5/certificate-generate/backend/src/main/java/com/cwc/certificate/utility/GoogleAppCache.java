package com.cwc.certificate.utility;

import com.cwc.certificate.model.GoogleScript;
import com.cwc.certificate.repository.GoogleScriptRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Component
@Slf4j
public class GoogleAppCache {

    private final GoogleScriptRepository googleScriptRepository;

    @Autowired
    public GoogleAppCache(GoogleScriptRepository googleScriptRepository) {
        this.googleScriptRepository = googleScriptRepository;
    }
    public Map<String,String> googleAppCache;

    @PostConstruct
    public void init(){
        log.info("Initializing Google Script cache from the database...");
        googleAppCache = new HashMap<>();
        List<GoogleScript> googleScriptList = googleScriptRepository.findAll();
        if (googleScriptList.isEmpty()){
            log.warn("No Google Scripts found in the database to initialize the cache.");
        }else {
            googleScriptList.forEach(googleScript -> googleAppCache.put(googleScript.getGoogleId(),googleScript.getScriptUrl()));
        }
        log.info("Google Script cache initialized with {} entries.", googleAppCache.size());
    }
}
