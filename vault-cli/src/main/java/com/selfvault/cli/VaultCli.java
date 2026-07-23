package com.selfvault.cli;

import java.net.http.HttpClient;
import java.time.Duration;

public class VaultCli {
    private final HttpClient httpClient;
    private final String serverUrl;

    public VaultCli(HttpClient httpClient, String serverUrl) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.serverUrl = serverUrl;
    }
}
