package com.cwc.certificate.config.drools;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/08/04
 */

@Configuration
@Slf4j
public class DroolsConfig {

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        log.info("KieContainer initialized: {} " , kieContainer);
        return kieContainer;
    }

    @Bean
    public KieSession kieSession(KieContainer kieContainer) {
        KieSession kSession = kieContainer.newKieSession("ksession-rules");
        log.info("KieSession created: {} " , kSession);
        return kSession;
    }
}
