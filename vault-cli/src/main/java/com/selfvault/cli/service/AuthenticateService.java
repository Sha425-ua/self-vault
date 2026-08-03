package com.selfvault.cli.service;

import com.selfvault.cli.client.VaultApiClient;

import java.util.Base64;

public class AuthenticateService {
    private final VaultApiClient apiClient;

    public AuthenticateService(VaultApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public byte[] getUserSalt(String username) {
        return Base64.getDecoder().decode(apiClient.getSalt(username).salt());
    }
}
