package com.cwc.certificate.controller;


import com.cwc.certificate.dto.request.MessageRequest;
import com.cwc.certificate.config.ratelimit.aop.RateLimited;
import com.cwc.certificate.service.ExcelService;
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
@CrossOrigin("*")
@RequestMapping("/api/v2/excel")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Excel Generate", description = "Manage Excel File")
public class ExcelGenerateController {

   @Autowired
   private final ExcelService excelService;


    @Operation(summary = "Export data in excel")
    @ApiResponses({
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @RateLimited
    @GetMapping("/generate")
    public ResponseEntity<byte[]> exportDataToExcelAd(@RequestParam String gateway) {
        ResponseEntity<byte[]> exportedDataToExcel = this.excelService.processExportDataToExcel(gateway);
        log.info("Excel file generated successfully of type : " + gateway);
        return new ResponseEntity<>(exportedDataToExcel, HttpStatus.OK).getBody();
    }


    @RequestMapping(value = "/head/endpoint",method = RequestMethod.HEAD)
    public ResponseEntity<MessageRequest> getHeadEndpoints(@RequestHeader("Content-Length") String contentLength) {
        log.info("Head request for Excel endpoint");
        MessageRequest messageRequest = MessageRequest.builder()
                .message("Content Length :: " + contentLength)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageRequest);
    }

    @RequestMapping(value = "/options/endpoint",method = RequestMethod.OPTIONS)
    public ResponseEntity<?> getOptionsEndpoints() {
        log.info("Options request for Excel endpoint");
        String allMethods = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
        MessageRequest messageRequest = MessageRequest.builder()
                .message("All Methods List :: " + allMethods)
                .build();
        return new ResponseEntity<>(messageRequest,HttpStatus.OK);
    }

}
