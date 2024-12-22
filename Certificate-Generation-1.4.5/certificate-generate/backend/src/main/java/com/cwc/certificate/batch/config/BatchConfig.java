package com.cwc.certificate.batch.config;


import com.cwc.certificate.batch.listener.JobCompletionNotificationListener;
import com.cwc.certificate.batch.processor.CandidateProcessor;
import com.cwc.certificate.batch.writer.CandidateWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/07
 */

@Configuration
@Slf4j
@EnableBatchProcessing
public class BatchConfig {
    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;
    private CandidateProcessor candidateProcessor;
    private CandidateWriter candidateWriter;

    @Autowired
    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, CandidateProcessor candidateProcessor, CandidateWriter candidateWriter) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.candidateProcessor = candidateProcessor;
        this.candidateWriter = candidateWriter;
    }

    @Bean
    public Job processCandidatesJob(JobCompletionNotificationListener listener) {
        return new JobBuilder("processCandidatesJob", jobRepository)
                .listener(listener)
                .start(processCandidatesStep())
                .build();
    }

    @Bean
    public Step processCandidatesStep() {
        return new StepBuilder("processCandidatesStep", jobRepository)
                .<String, String>chunk(10, transactionManager)
                .reader(certificateIdReader(null))
                .processor(candidateProcessor)
                .writer(candidateWriter)
                .build();
    }

    @StepScope
    @Bean
    public ListItemReader<String> certificateIdReader(
            @Value("#{jobParameters['selectedIds']}") List<String> selectedIds) {
        return new ListItemReader<>(selectedIds);
    }

}
