package com.selfvault.client.config;

public record AppConfig(
   int appTimerTimeoutDuration,
   String hostAddress
) {
    public AppConfig() {
        this(15, "http://localhost:7070");
    }
}
