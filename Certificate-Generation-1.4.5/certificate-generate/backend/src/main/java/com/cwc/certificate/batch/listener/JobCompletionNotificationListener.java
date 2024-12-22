package com.cwc.certificate.batch.listener;

import com.cwc.certificate.batch.writer.CandidateWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/07
 */

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    private final CandidateWriter candidateWriter;
    private final BatchNotificationService notificationService;

    @Autowired
    public JobCompletionNotificationListener(CandidateWriter candidateWriter,BatchNotificationService notificationService) {
        this.candidateWriter = candidateWriter;
        this.notificationService = notificationService;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().isUnsuccessful()) {
            log.error("Job failed with status: {}", jobExecution.getStatus());
            notificationService.notifyJobFailure(jobExecution);
            return;
        }

        log.info("Job completed successfully with status: {}", jobExecution.getStatus());

        List<String> responses = candidateWriter.getResponses();

        if (responses != null && !responses.isEmpty()) {
            log.info("Responses collected from CandidateWriter: {}", responses);
            notificationService.notifyJobSuccess(jobExecution,responses);
        } else {
            log.warn("No responses collected from CandidateWriter.");
        }
    }

    public CandidateWriter getCandidateWriter() {
        return candidateWriter;
    }
}

