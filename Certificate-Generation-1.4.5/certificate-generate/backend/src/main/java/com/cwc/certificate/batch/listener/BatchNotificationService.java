package com.cwc.certificate.batch.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/07
 */

@Service
@Slf4j
public class BatchNotificationService {

    public void notifyJobFailure(JobExecution jobExecution) {
        String message = String.format("Job %s failed with status: %s", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
        log.info("Sending failure notification: " + message);
    }

    public void notifyJobSuccess(JobExecution jobExecution, List<String> responses) {
        String message = String.format("Job %s completed successfully with status: %s. Responses: %s",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus(),
                responses);
        log.info("Sending success notification: " + message);
    }
}

