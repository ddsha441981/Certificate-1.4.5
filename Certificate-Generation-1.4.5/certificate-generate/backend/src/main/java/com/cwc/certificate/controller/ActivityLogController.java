package com.cwc.certificate.controller;


import com.cwc.certificate.dto.response.ActivityLogResponse;
import com.cwc.certificate.model.ActivityLog;
import com.cwc.certificate.service.ActivityLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/02
 */

@RestController
@RequestMapping("/api/v4/activity")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
@Tag(name = "Activity Log", description = "Activity Log API")
public class ActivityLogController {

    private final ActivityLogService activityLogService;



    @Operation(summary = "Retrieve all activity log list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ActivityLog[].class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @GetMapping("/logs")
    public ResponseEntity<List<ActivityLogResponse>> getActivityLogsList(){
        List<ActivityLogResponse> logActivityDetails = this.activityLogService.getLogActivityDetails();
        return new ResponseEntity<>(logActivityDetails, HttpStatus.OK);
    }

}
