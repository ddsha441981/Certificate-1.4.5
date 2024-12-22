package com.cwc.certificate.controller;

import com.cwc.certificate.validations.APIResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@RequestMapping("/api/v1/global")
@RestController
@Slf4j
public class GlobalController { 


    public ResponseEntity<APIResponse<String>> global() {
        log.info("Global called");
        APIResponse<String> apiResponse = APIResponse.<String>builder()
                .code(200)
                .message("Controller called")
                .responseData("Our test controller called.........")
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @GetMapping("/dir")
    public JsonObject  getFolderStructure() {
        final String SCRIPT_WEB_APP_URL = "https://script.google.com/macros/s/AKfycbzeSzYg1XPAH9CLTzyfiDiuSUT7v68ni_XWNFVn3BhBqaTiGBZJUdMzoNfKTNBOkSIk/exec";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(SCRIPT_WEB_APP_URL, String.class);
        JsonObject folderStructure = JsonParser.parseString(result).getAsJsonObject();
        return folderStructure;
    }

}

