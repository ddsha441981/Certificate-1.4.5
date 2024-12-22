package com.cwc.certificate.controller;

import com.cwc.certificate.batch.listener.JobCompletionNotificationListener;
import com.cwc.certificate.service.CertificateService;
import com.cwc.certificate.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/09/07
 */

@RestController
@RequestMapping("/api/v1/certificate")
@CrossOrigin("*")
@Slf4j
public class BatchController {
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final JobCompletionNotificationListener listener;
    private final Job processCandidatesJob;
    private final CertificateService certificateService;
    private final CompanyService companyService;

    @Autowired
    public BatchController(JobLauncher jobLauncher, JobExplorer jobExplorer, JobCompletionNotificationListener listener,Job processCandidatesJob, CertificateService certificateService, CompanyService companyService) {
        this.jobLauncher = jobLauncher;
        this.jobExplorer = jobExplorer;
        this.listener = listener;
        this.processCandidatesJob = processCandidatesJob;
        this.certificateService = certificateService;
        this.companyService = companyService;
    }

    /**
     * @Param : Now it will handle millions of requests
     * */
    @Async
    public CompletableFuture<JobExecution> processJob(List<String> selectedIds) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("selectedIds", String.join(",", selectedIds))
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(processCandidatesJob, jobParameters);

            while (jobExecution.isRunning()) {
                Thread.sleep(1000);
                jobExecution = jobExplorer.getJobExecution(jobExecution.getId());
            }

            listener.afterJob(jobExecution);
            return CompletableFuture.completedFuture(jobExecution);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @PostMapping("/process/batch")
    public ResponseEntity<String> processSelectedCandidates(@RequestBody Map<String, List<String>> request) {
        List<String> selectedIds = request.get("selectedIds");

        if (selectedIds == null || selectedIds.isEmpty()) {
            return ResponseEntity.badRequest().body("No candidate IDs provided.");
        }

        try {
            CompletableFuture<JobExecution> future = processJob(selectedIds);
            future.thenAccept(jobExecution -> {
                List<String> responses = listener.getCandidateWriter().getResponses();
                log.info("Successfully documents generated from batch: {}", responses);
            });

            return new ResponseEntity<>("Batch processing started", HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error starting batch job: " + e.getMessage());
        }
    }

    //Removed Redundancy

//    @PostMapping("/process/batch")
//    public ResponseEntity<String> processSelectedCandidates(@RequestBody Map<String, List<String>> request) {
//        List<String> selectedIds = request.get("selectedIds");
//        if (selectedIds == null || selectedIds.isEmpty()) {
//            return ResponseEntity.badRequest().body("No candidate IDs provided.");
//        }
//
//        try {
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addString("selectedIds", String.join(",", selectedIds))
//                    .addLong("time", System.currentTimeMillis())
//                    .toJobParameters();
//
//            JobExecution jobExecution = jobLauncher.run(processCandidatesJob, jobParameters);
//
//            while (jobExecution.isRunning()) {
//                Thread.sleep(1000);
//                jobExecution = jobExplorer.getJobExecution(jobExecution.getId());
//            }
//            List<String> responses = listener.getCandidateWriter().getResponses();
//            listener.afterJob(jobExecution);
//            log.info("Successfully documents generated from batch :{}", responses);
//            return new ResponseEntity("Successfully documents generated from batch ", HttpStatus.OK);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error starting batch job: " + e.getMessage());
//        }
//    }
}
