package com.cwc.certificate.service.impl;

import com.cwc.certificate.adapters.excel.processor.ExcelProcessor;
import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.request.ActivityLogRequest;
import com.cwc.certificate.observer.Message;
import com.cwc.certificate.observer.MessagePublisher;
import com.cwc.certificate.service.ActivityLogService;
import com.cwc.certificate.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/02/14 Update 20/08/2024
 */

@Service
@Slf4j
public class ExcelGeneratorServiceImpl implements ExcelService {

    private final ActivityLogService activityLogService;
    private final MessagePublisher publisher;
    private Map<String, ExcelProcessor> excelProcessorMap;
    /* CertificateExcelAdapter , CertificateExcelAdapter.class */

    public ExcelGeneratorServiceImpl(List<ExcelProcessor> excelProcessors, ActivityLogService activityLogService,MessagePublisher publisher) {
        this.activityLogService = activityLogService;
        this.publisher = publisher;
        excelProcessorMap = excelProcessors
                .stream()
                .collect(Collectors.toMap(processor -> processor
                        .getClass()
                        .getSimpleName(),
                        Function.identity())
                );

        //Subscribe Observer
        publisher.attach(activityLogService);
    }

    @Override
    public ResponseEntity<byte[]> processExportDataToExcel(String gateway) {
        gateway = gateway.substring(0, 1).toUpperCase() + gateway.substring(1).toLowerCase();
        ExcelProcessor excelProcessor = excelProcessorMap.get(gateway + ConstantValue.DEFAULT_EXCEL_ADAPTER_NAME);
        if (excelProcessor != null) {
            ResponseEntity<byte[]> makeExcelProcess = excelProcessor.makeExcelProcess(gateway);

            //Notify Observer
            publisher.notifyUpdate(new Message("EXCEL generation for: " + gateway + " has started."));
            // Log the action in ActivityLog
            log.info("Activity log trigger to capture excel generation details...");
            ActivityLogRequest activityLogRequest = ActivityLogRequest.builder()
                    .actionType("Excel Generated")
                    .entityName(gateway)
                    .actionTimestamp(LocalDateTime.now())
                    .details("Excel generated for: " + gateway
                            + " on date: " + LocalDateTime.now().toString())
                    .build();
            activityLogService.logActivity(activityLogRequest);
            //detached Observer
            publisher.detach(activityLogService);
            return makeExcelProcess;
        } else {
            System.out.println("No Excel processor found for gateway: " + gateway);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
