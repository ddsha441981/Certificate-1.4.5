package com.cwc.log.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/07/14
 */

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate (){
        return new RestTemplate();
    }
}
