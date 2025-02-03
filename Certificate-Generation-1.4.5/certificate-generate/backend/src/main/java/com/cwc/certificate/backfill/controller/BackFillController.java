package com.cwc.certificate.backfill.controller;

import com.cwc.certificate.backfill.dto.BackTableRequest;
import com.cwc.certificate.backfill.service.BackTableService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/12/16
 */
@RestController
@RequestMapping("/api/v1/backfill")
public class BackFillController {


    private final  BackTableService backTableService;

    public BackFillController(BackTableService backTableService) {
        this.backTableService = backTableService;
    }

    @PostMapping("/dynamic")
    public String triggerDynamicBackfill(@RequestBody BackTableRequest request) {
        backTableService.backfillEntity(
                request.getEntityName(),
                request.getColumnName(),
                request.getNewValue(),
                request.getBatchSize()
        );
        return "Dynamic backfill completed!";
    }
}
