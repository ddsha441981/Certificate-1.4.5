package com.cwc.certificate.config.ratelimit.aop;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.exceptions.model.RateLimitExceededException;
import com.cwc.certificate.config.ratelimit.config.RateLimitConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitAspect {

    private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

    @Autowired
    private final RateLimitConfig rateLimitConfig;

    /**
     * Executed by each call of a method annotated with {@link RateLimited} which should be an HTTP endpoint.
     * Counts calls per remote address. Calls older than {rateLimitConfig.getRateLimit()} milliseconds will be forgotten. If there have
     * been more than {@link #rateLimit} calls within {rateLimitConfig.getRateDuration()} milliseconds from a remote address, a {@link RateLimitExceededException}
     * will be thrown.
     * @throws RateLimitExceededException iff rate limit for a given remote address has been exceeded
     */

    @Before("@annotation(com.cwc.certificate.config.ratelimit.aop.RateLimited)")
    public void rateLimit() {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        final String key = getKey(request);
        log.info("Rate limit IP key is {} ", key);
        final long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(key, Collections.synchronizedList(new ArrayList<>()));
        requestCounts.get(key).add(currentTime);
        cleanUpRequestCounts(currentTime);
        if (requestCounts.get(key).size() > rateLimitConfig.getRateLimit()) {
            throw new RateLimitExceededException(String.format(ConstantValue.RATE_LIMIT_ERROR_MESSAGE, request.getRequestURI(), key, rateLimitConfig.getRateDuration()));
        }
    }

    private void cleanUpRequestCounts(final long currentTime) {
        requestCounts.values().forEach(l -> {
            synchronized (l) {
                l.removeIf(t -> timeIsTooOld(currentTime, t));
            }
        });
    }


    private boolean timeIsTooOld(final long currentTime, final long timeToCheck) {
        return currentTime - timeToCheck > rateLimitConfig.getRateDuration();
    }

    private String getKey(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}