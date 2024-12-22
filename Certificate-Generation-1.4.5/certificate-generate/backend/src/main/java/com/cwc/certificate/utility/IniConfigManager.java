package com.cwc.certificate.utility;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//@Component
public class IniConfigManager {

    private final Map<String, INIConfiguration> configMap = new HashMap<>();

    public IniConfigManager() {
        configMap.put("config", loadIniFile("credential/config.ini"));
    }

    private INIConfiguration loadIniFile(String filePath) {
        Configurations configs = new Configurations();
        try {
            File iniFile = new File(getClass().getClassLoader().getResource(filePath).getFile());
            return configs.ini(iniFile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + filePath, e);
        }
    }

    public INIConfiguration getConfig(String configName) {
        return configMap.get(configName);
    }
}

