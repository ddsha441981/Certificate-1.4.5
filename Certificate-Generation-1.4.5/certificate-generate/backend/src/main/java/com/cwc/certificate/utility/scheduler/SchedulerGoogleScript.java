package com.cwc.certificate.utility.scheduler;

import com.cwc.certificate.utility.GoogleAppCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Component
@Slf4j
public class SchedulerGoogleScript {

    private  final GoogleAppCache googleAppCache;

    @Autowired
    public SchedulerGoogleScript(GoogleAppCache googleAppCache) {
        this.googleAppCache = googleAppCache;
    }

    /**
     * Refreshes the cache of Google Scripts every 10 minutes.
     * This ensures updated data without requirring a manual refresh after every script URL update.
     */
    @Scheduled(cron = "0 0/10 * ? * *")
    public void refreshGoogleScriptCache() {
        log.info("Scheduled cache refresh for Google Scripts started at {}", new Date());
        try {
            googleAppCache.init();
            log.info("Google Script cache successfully refreshed at {}", new Date());
        } catch (Exception e) {
            log.error("Error occurred while refreshing the Google Script cache: {}", e.getMessage(), e);
        }
    }
}
