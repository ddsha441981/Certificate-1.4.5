package com.cwc.certificate.controller;

import com.cwc.certificate.model.Url;
import com.cwc.certificate.dto.request.MessageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@RestController
@RequestMapping("/api/v1/properties")
@CrossOrigin("*")
@Slf4j
@Tag(name = "Properties", description = "Properties management API")
public class PropertiesController {

    @Value("${google_url.script}")
    private String googleUrlScript;


    @Operation(summary = "Create a new google Script Url after deployment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = PropertiesController.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })
    @PostMapping("/updateUrl")
    public String updateUrl(@RequestBody Url url) {
        String updatedUrl = url.getNewUrl();
        try {
            Properties properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("google_url.properties");
            if (inputStream != null) {
                properties.load(inputStream);

                String decodedUrl = URLDecoder.decode(updatedUrl, StandardCharsets.UTF_8);

                properties.setProperty("google_url.script", decodedUrl);

                try (OutputStream outputStream = new FileOutputStream("src/main/resources/google_url.properties")) {
                    properties.store(outputStream, null);
                }

                // Update property value
                googleUrlScript = decodedUrl;
                log.info(" updated successfully {} ",googleUrlScript);

                return "URL updated successfully";
            } else {
                log.info("Error Properties file not found: {}  " + url);
                return "Error: Properties file not found";
            }
        } catch (IOException e) {
//            e.printStackTrace();
            log.info("Error updating URL: " + e.getMessage());
            return "Error updating URL: " + e.getMessage();
        }
    }

    @RequestMapping(value = "/head/endpoint",method = RequestMethod.HEAD)
    public ResponseEntity<MessageRequest> getHeadEndpoints(@RequestHeader("Content-Length") String contentLength) {
        log.info("Head request for properties endpoint");
        MessageRequest messageRequest = MessageRequest.builder()
                .message("Content Length :: " + contentLength)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageRequest);
    }

    @RequestMapping(value = "/options/endpoint",method = RequestMethod.OPTIONS)
    public ResponseEntity<?> getOptionsEndpoints() {
        log.info("Options request for properties endpoint");
        String allMethods = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
        MessageRequest messageRequest = MessageRequest.builder()
                .message("All Methods List :: " + allMethods)
                .build();
        return new ResponseEntity<>(messageRequest,HttpStatus.OK);
    }

}
