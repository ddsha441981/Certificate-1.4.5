package com.cwc.certificate.controller;

import com.cwc.certificate.exceptions.model.GoogleScriptNotVaildException;
import com.cwc.certificate.model.GoogleScript;
import com.cwc.certificate.dto.request.GoogleScriptRequest;
import com.cwc.certificate.dto.request.MessageRequest;
import com.cwc.certificate.config.ratelimit.aop.RateLimited;
import com.cwc.certificate.service.GoogleScriptService;
import com.cwc.certificate.utility.GoogleAppCache;
import com.cwc.certificate.utility.URLExtractor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@RestController
@RequestMapping("/api/v1/script")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Google Script", description = "Google Script Controller API")
public class GoogleScriptController {

    @Autowired
    private final GoogleScriptService googleScriptService;

    @Autowired
    private final GoogleAppCache googleAppCache;


    @Operation(summary = "Update Google Script information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = GoogleScript.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })
    @RateLimited
    @PutMapping("/update/{googleId}")
    public ResponseEntity<GoogleScript> updateGoogleScript(@RequestBody GoogleScriptRequest googleScriptRequest, @PathVariable("googleId") String googleId) {
        if (googleScriptRequest.getScriptUrl().isEmpty()) {
            log.info("Google Script Url is empty");
            throw  new GoogleScriptNotVaildException("Google Script Url is empty or not valid url or invalid url format");
        }
        String scriptUrl = googleScriptRequest.getScriptUrl();
        String extractKey = URLExtractor.extractKey(scriptUrl);
        if (extractKey == null) {
            log.info("Google Script Url is not valid");
            throw new GoogleScriptNotVaildException("Google Script Url is empty or not valid url or invalid url format");
        }
        log.info("Extracted key: {}", extractKey);
        googleScriptRequest.setScriptUrl(extractKey);

        GoogleScript updatedGoogleScript = this.googleScriptService.updateGoogleScript(googleScriptRequest,googleId);
        /**
         * After updating call init method of GoogleAppCache to loading beans and reflateded data from database no need to reloiad app
         */
        googleAppCache.init();
        log.info("init method of GoogleAppCache is called and Google Script updated successfully");
        return new ResponseEntity<>(updatedGoogleScript, HttpStatus.OK);
    }

    @RequestMapping(value = "/head/endpoint",method = RequestMethod.HEAD)
    public ResponseEntity<MessageRequest> getHeadEndpoints(@RequestHeader("Content-Length") String contentLength) {
        log.info("Head request for Google app-script endpoint");
        MessageRequest messageRequest = MessageRequest.builder()
                .message("Content Length :: " + contentLength)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageRequest);
    }

    @RequestMapping(value = "/options/endpoint",method = RequestMethod.OPTIONS)
    public ResponseEntity<?> getOptionsEndpoints() {
        log.info("Options request for Google app-script endpoint");
        String allMethods = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
        MessageRequest messageRequest = MessageRequest.builder()
                .message("All Methods List :: " + allMethods)
                .build();
        return new ResponseEntity<>(messageRequest,HttpStatus.OK);
    }

}
