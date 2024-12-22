package com.cwc.log.consumer.service;

import com.cwc.log.consumer.adpaters.processor.LogProcessor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/07/14
 */

@Service
@Slf4j
public class LogConsumerService {
    private final Map<String, LogProcessor> logProcessorMap;
    public static final  String LOG_FILE_PATH = "/logs/certificate.log";

    @PostConstruct
    public void generate(){
        log.info("Log service initialized-------------------------");
    }

    @Autowired
    public LogConsumerService(List<LogProcessor> logProcessors) {
        logProcessorMap = logProcessors.stream()
                .collect(Collectors
                        .toMap(processor -> processor
                        .getClass()
                        .getSimpleName(),
                        Function.identity())
                );
    }

//    @Scheduled(fixedRate =  60000)
//    public void processLogs() {
//        String gateway = "ElasticSearch";
//        try(BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))){
//            String line;
//            while((line = reader.readLine()) != null){
//                log.info("Processing Log " + line);
//                consumeLogs(gateway,line);
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//    }

//    private void consumeLogs(String logMessage,String gateway) {
//        LogProcessor logProcessor = logProcessorMap.get(gateway + "Adapter");
//        if (logProcessor != null) {
//            logProcessor.captureLogs(gateway, logMessage);
//        } else {
//            log.error("No log processor found for gateway: {}", gateway);
//        }
//
//    }

    @KafkaListener(topics = "${kafka.topic.logs}", groupId = "${kafka.group.id}")
    public void generateLogs(String logMessage) {
        // TODO: If you need to manage multiple logs with different logging systems,
        //  update your logic here to pass the gateway dynamically from the controller or use a database.

        String gateway = "ElasticSearch";
        LogProcessor logProcessor = logProcessorMap.get(gateway + "Adapter");
        if (logProcessor != null) {
            logProcessor.captureLogs(gateway, logMessage);
        } else {
            log.error("No log processor found for gateway: {}", gateway);
        }
    }
}