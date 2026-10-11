package com.selfvault.client.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;

public class ConfigLoader {

    private final ObjectMapper objectMapper;
    public final AppConfig appConfig;
    private final File file;

    public ConfigLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.file = AppPaths.getConfigFilePath().toFile();
        this.appConfig = load();
    }

    public ConfigLoader() {
        this(new ObjectMapper()
                .configure(DeserializationFeature
                        .FAIL_ON_UNKNOWN_PROPERTIES, true)
                .enable(SerializationFeature
                        .INDENT_OUTPUT)
        );
    }

    public AppConfig load() {
        try {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            if (file.exists()) {
                return objectMapper.readValue(file, AppConfig.class);
            } else {
                AppConfig defaultConfig = new AppConfig();
                objectMapper.writeValue(file, defaultConfig);
                return defaultConfig;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading config file: ", e);
        }
    }
}
