package com.cwc.certificate.service.drive.impl;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.model.drive.Folder;
import com.cwc.certificate.service.drive.GoogleDriveService;
import com.cwc.certificate.utility.GoogleAppCache;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/05/02
 */

@Service
@Slf4j
public class GoogleDriveServiceImpl implements GoogleDriveService {

    private final RestTemplate restTemplate;
    private final GoogleAppCache googleAppCache;

    @Autowired
    public GoogleDriveServiceImpl(RestTemplate restTemplate, GoogleAppCache googleAppCache) {
        this.restTemplate = restTemplate;
        this.googleAppCache = googleAppCache;
    }

    @Override
    public Folder getFolderStructure() throws Exception {
        String SCRIPT_WEB_APP_URL = googleAppCache.googleAppCache.get(ConstantValue.DEFAULT_DRIVE_NAME);
        String staticScript = ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL_TYPE + SCRIPT_WEB_APP_URL + ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL_SEPARATE + ConstantValue.DEFAULT_GOOGLE_APP_SCRIPT_URL;

        log.info("Fetching JSON response from Google Apps Script");
        String result = restTemplate.getForObject(staticScript, String.class);
        log.info("Fetched JSON: " + result);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        log.info("Converting JSON string to Folder object");
        Folder folder = objectMapper.readValue(result, Folder.class);

        log.info("Deserialized Folder: " + folder);

        return folder;
    }

    @Override
    public Optional<Folder> findFolderByName(Folder folder, String targetFolderName) {
        if (folder.getFolderName().equalsIgnoreCase(targetFolderName)) {
            return Optional.of(folder);
        }

        log.info("Recursively searching in sub folders");
        for (Folder subfolder : folder.getCompanies()) {
            Optional<Folder> found = findFolderByName(subfolder, targetFolderName);
            if (found.isPresent()) {
                return found;
            }
        }
        for (Folder subfolder : folder.getUsers()) {
            Optional<Folder> found = findFolderByName(subfolder, targetFolderName);
            if (found.isPresent()) {
                return found;
            }
        }
        return Optional.empty();
    }

}
