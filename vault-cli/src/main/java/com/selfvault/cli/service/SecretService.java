package com.selfvault.cli.service;

import com.selfvault.cli.client.VaultApiClient;
import com.selfvault.crypto.AesEncryptionService;
import com.selfvault.crypto.AuthHashService;
import com.selfvault.crypto.KeyDerivationService;
import com.selfvault.domain.model.SecretRequestDto;

import java.util.Base64;

public class SecretService {
    private final VaultApiClient apiClient;
    private final AuthenticateService authenticateService;

    public SecretService(VaultApiClient apiClient, AuthenticateService authenticateService) {
        this.apiClient = apiClient;
        this.authenticateService = authenticateService;
    }

    public void addNewSecret(String username, String title, char[] secret, char[] masterPassword) throws Exception {
        byte[] salt;
        byte[] masterKey = null;

        try {
            salt = authenticateService.getUserSalt(username);
            masterKey = KeyDerivationService.deriveKey(masterPassword, salt);


            byte[] encryptedData = AesEncryptionService.encryptSecret(secret, masterKey);
            KeyDerivationService.wipe(secret);
            String encryptedDataString = Base64.getEncoder().encodeToString(encryptedData);

            String authHash = AuthHashService.generateAuthHash(masterKey);
            KeyDerivationService.wipe(masterKey);

            SecretRequestDto requestDto = new SecretRequestDto(title, encryptedDataString);
            apiClient.sendSecret(username, authHash, requestDto);
        } finally {
            KeyDerivationService.wipe(secret);
            KeyDerivationService.wipe(masterKey);
            KeyDerivationService.wipe(masterPassword);
        }
    }

    public void deleteSecret(String username, String title, char[] masterPassword) throws Exception {
        byte[] salt;
        byte[] masterKey = null;

        try {
            salt = authenticateService.getUserSalt(username);
            masterKey = KeyDerivationService.deriveKey(masterPassword, salt);

            String authHash = AuthHashService.generateAuthHash(masterKey);
            KeyDerivationService.wipe(masterKey);

            apiClient.deleteSecret(username, authHash, title);
        } finally {
            KeyDerivationService.wipe(masterPassword);
            KeyDerivationService.wipe(masterKey);
        }
    }
}
