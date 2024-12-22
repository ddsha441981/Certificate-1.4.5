package com.cwc.log.consumer.adpaters.processor;

public interface LogProcessor {

    void captureLogs(String gateway,String logMessage);
}
