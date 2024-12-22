package com.cwc.certificate.service.impl;

import com.cwc.certificate.adapters.pdf.processor.PdfProcessor;
import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.request.ActivityLogRequest;
import com.cwc.certificate.observer.Message;
import com.cwc.certificate.observer.MessagePublisher;
import com.cwc.certificate.service.ActivityLogService;
import com.cwc.certificate.service.GeneratePdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/02/14 Update 20/08/2024
 */

@Service
@Slf4j
public class PdfGeneratorServiceImpl implements GeneratePdfService {

    private final ActivityLogService activityLogService;
    private final MessagePublisher publisher;
    private Map<String, PdfProcessor> pdfProcessorMap;
    /* CertificatePdfAdapter , CertificatePdfAdapter.class */

    public PdfGeneratorServiceImpl(List<PdfProcessor> pdfProcessors, ActivityLogService activityLogService,MessagePublisher publisher) {
        this.activityLogService = activityLogService;
        this.publisher = publisher;
        pdfProcessorMap = pdfProcessors.stream()
                .collect(Collectors.toMap(processor ->
                        processor.getClass().getSimpleName(), Function.identity()));

        //Subscribe Observer
        publisher.attach(activityLogService);
    }

    @Override
    public ResponseEntity<byte[]> processPdf(String gateway) {
        if (gateway != null && !gateway.isEmpty()) {
            gateway = gateway.substring(0, 1).toUpperCase() + gateway.substring(1).toLowerCase();
        }
        // Call the adapter
        PdfProcessor pdfProcessor = pdfProcessorMap.get(gateway + ConstantValue.DEFAULT_PDF_ADAPTER_NAME);
        if (pdfProcessor != null) {
            ResponseEntity<byte[]> makePdfProcess = pdfProcessor.makePdfProcess(gateway);

            //Notify Observer
            publisher.notifyUpdate(new Message("PDF generation for: " + gateway + " has started."));

            // Log the action in ActivityLog
            log.info("Activity log trigger to capture pdf generation details...");
            ActivityLogRequest activityLogRequest = ActivityLogRequest.builder()
                    .actionType("Pdf Generated")
                    .entityName(gateway)
                    .actionTimestamp(LocalDateTime.now())
                    .details("PDF generated for: " + gateway
                            + " on date: " + LocalDateTime.now().toString())
                    .build();
            activityLogService.logActivity(activityLogRequest);
            //Detach Observer
            publisher.detach(activityLogService);
            return makePdfProcess;
        } else {
            System.out.println("No PDF processor found for gateway: " + gateway);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
