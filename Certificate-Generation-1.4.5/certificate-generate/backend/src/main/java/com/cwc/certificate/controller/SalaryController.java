package com.cwc.certificate.controller;

import com.cwc.certificate.model.Certificate;
import com.cwc.certificate.dto.request.MessageRequest;
import com.cwc.certificate.config.ratelimit.aop.RateLimited;
import com.cwc.certificate.service.CertificateSalaryService;
import com.cwc.certificate.utility.DateUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/salary")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Salary Management API", description = "API to manage and retrieve salary-related information")
public class SalaryController {

    @Autowired
    private final CertificateSalaryService certificateSalaryService;


    @Operation(summary = "Salary generation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SalaryController[].class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @RateLimited
    @GetMapping("/generate")
    public ResponseEntity<?> generateSalary(
            @RequestParam("salaryFrom") String salaryFrom,
            @RequestParam("salaryTo") String salaryTo,
            //@RequestParam("salaryMode") String salaryMode,
            @Parameter(name =  "Salary mode (3months, 6months)", required = true) @RequestParam("salaryMode") String salaryMode,
            @RequestParam("certificateId") int certificateId
    ) {
        //date format change
        String s1 = DateUtils.convertDateFormat(salaryFrom);
        String s2 = DateUtils.convertDateFormat(salaryTo);
        log.info("Salary from: {}, Salary to: {}, Salary mode: {}, Certificate ID: {}",
                salaryFrom, salaryTo, salaryMode, certificateId);
        String generateSalarySlip = this.certificateSalaryService.generateSalarySlip(s1, s2, salaryMode, certificateId);
        return new  ResponseEntity<>(generateSalarySlip, HttpStatus.OK);
    }

    @Operation(summary = "Head")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = SalaryController.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RequestMapping(value = "/head/endpoint",method = RequestMethod.HEAD)
    public ResponseEntity<MessageRequest> getHeadEndpoints(@RequestHeader("Content-Length") String contentLength) {
        log.info("Head request for salary endpoint");
        MessageRequest messageRequest = MessageRequest.builder()
                .message("Content Length :: " + contentLength)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageRequest);
    }

    @Operation(summary = "Options")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RequestMapping(value = "/options/endpoint",method = RequestMethod.OPTIONS)
    public ResponseEntity<?> getOptionsEndpoints() {
        log.info("Options request for salary endpoint");
        String allMethods = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
        MessageRequest messageRequest = MessageRequest.builder()
                .message("All Methods List :: " + allMethods)
                .build();
        return new ResponseEntity<>(messageRequest,HttpStatus.OK);
    }
}
