package com.cwc.certificate.singleton;

import lombok.AllArgsConstructor;
import lombok.Getter;


//Singleton
@AllArgsConstructor
@Getter
public enum LoggingConfig {
    INSTANCE;

    private String logFilePath;
    private String logLevel;
    private String logFormat;
    LoggingConfig() {
        this.logFilePath = "/var/logs/app.log";
        this.logLevel = "INFO";
        this.logFormat = "[%d{yyyy-MM-dd HH:mm:ss}] [%p] %m%n";
    }

}
