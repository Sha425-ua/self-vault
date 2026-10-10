package com.selfvault.client.config;

import java.nio.file.Path;

public final class AppPaths {

    private static final String APP_DIR_NAME = "self-vault";
    private static final String CONFIG_FILE_NAME = "config.json";

    private AppPaths() {}

    public static Path getConfigDirPath() {
        String userHome = System.getProperty("user.home", ".");
        String osName = System.getProperty("os.name", "").toLowerCase();

        if (osName.contains("win")) {
            String appData = System.getenv("APPDATA");
            if (appData != null && !appData.isBlank()) {
                return Path.of(appData, APP_DIR_NAME);
            }
            return Path.of(userHome, "." + APP_DIR_NAME);
        }

        if (osName.contains("mac")) {
            return Path.of(userHome, "Library", "Application Support", APP_DIR_NAME);
        }

        String xdgConfig = System.getenv("XDG_CONFIG_HOME");
        if (xdgConfig != null && !xdgConfig.isBlank()) {
            return Path.of(xdgConfig, APP_DIR_NAME);
        }
        return Path.of(userHome, ".config", APP_DIR_NAME);
    }

    public static Path getConfigFilePath() {
        return getConfigDirPath().resolve(CONFIG_FILE_NAME);
    }
}

