package com.selfvault.cli.config;

import com.selfvault.client.config.AppConfig;

public class SessionManager {
    private final AppConfig appConfig;

    public SessionManager(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public String getHostAddress() {
        return appConfig.hostAddress();
    }
}
