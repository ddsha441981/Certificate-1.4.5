package com.cwc.certificate.config.ratelimit.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Configuration
public class RateLimitConfig {
    @Value("${APP_RATE_LIMIT:#{200}}")
    private int rateLimit;

    @Value("${APP_RATE_DURATIONINMS:#{60000}}")
    private long rateDuration;
}
