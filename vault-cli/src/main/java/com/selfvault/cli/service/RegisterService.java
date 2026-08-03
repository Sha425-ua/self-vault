package com.selfvault.cli.service;

import com.selfvault.cli.client.VaultApiClient;
import com.selfvault.crypto.AuthHashService;
import com.selfvault.crypto.KeyDerivationService;
import com.selfvault.domain.model.RegisterRequestDto;

import java.util.Base64;

public class RegisterService {
    private final VaultApiClient apiClient;

    public RegisterService(VaultApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public void register(String username, char[] masterPassword) throws Exception {
        byte[] salt = KeyDerivationService.generateSalt();
        byte[] masterKey = KeyDerivationService.deriveKey(masterPassword, salt);

        String authHash = AuthHashService.generateAuthHash(masterKey);

        KeyDerivationService.wipe(masterPassword);

        RegisterRequestDto dto = new RegisterRequestDto(
                username,
                authHash,
                Base64.getEncoder().encodeToString(salt)
        );

        apiClient.register(dto);
    }
}
