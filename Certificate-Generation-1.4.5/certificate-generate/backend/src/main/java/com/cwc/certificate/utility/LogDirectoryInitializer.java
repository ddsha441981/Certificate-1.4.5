package com.cwc.certificate.utility;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@Slf4j
//@Component
public class LogDirectoryInitializer {

//    @PostConstruct
    public static void initializeLogDirectory() {
        log.info("-----------------------------------------------------Class Loaded: LogDirectoryInitializer------------------------------------------------------------------------------------");
        String logDirectoryPath = System.getProperty("user.home") + "/Certificate-Logs/";
        Path logDirectory = Paths.get(logDirectoryPath);

        if (!Files.exists(logDirectory)) {
            try {
                Files.createDirectories(logDirectory);
               log.info("Log directory created: {} " , logDirectoryPath);
            } catch (Exception e) {
                log.info("Failed to create log directory: {}" , logDirectoryPath);
                e.printStackTrace();
            }
        }
    }
}
