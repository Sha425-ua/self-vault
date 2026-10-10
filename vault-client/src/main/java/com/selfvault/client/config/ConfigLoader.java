package com.selfvault.client.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;

public class ConfigLoader {

    private final ObjectMapper objectMapper;

    public ConfigLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    private final File file = AppPaths.getConfigDirPath().toFile();

    public ConfigLoader() {
        this(new ObjectMapper()
                .configure(DeserializationFeature
                        .FAIL_ON_UNKNOWN_PROPERTIES, true)
                .enable(SerializationFeature
                        .INDENT_OUTPUT));
    }

    public final AppConfig appConfig = load();

    public AppConfig load() {
        try {
            file.getParentFile().mkdirs();

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
