package com.cwc.certificate.batch.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/07
 */

@Component
@Slf4j
public class CandidateProcessor implements ItemProcessor<String, String> {
    @Override
    public String process(String certificateId) throws Exception {
       log.info("Processing certificateId: " + certificateId);
        return certificateId;
    }
}

