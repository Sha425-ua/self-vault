package com.selfvault.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfvault.client.VaultApiClient;
import com.selfvault.client.util.CredentialsValidator;
import com.selfvault.crypto.AesEncryptionService;
import com.selfvault.crypto.AuthHashService;
import com.selfvault.crypto.KeyDerivationService;
import com.selfvault.domain.model.SecretDto;
import com.selfvault.domain.model.SecretRequestDto;

import java.util.Base64;
import java.util.List;

public class SecretService {
    private final VaultApiClient apiClient;
    private final ObjectMapper objectMapper;

    public SecretService(VaultApiClient apiClient) {
        this(apiClient, new ObjectMapper());
    }

    public SecretService(VaultApiClient apiClient, ObjectMapper objectMapper) {
        this.apiClient = apiClient;
        this.objectMapper = objectMapper;
    }

    public void addNewSecret(
            String username,
            byte[] masterKey,
            SecretDto dto
    ) throws Exception {

        CredentialsValidator.validate(username, masterKey);

        if (dto.getSecret() == null || dto.getSecret().length == 0) {
            throw new IllegalArgumentException("Secret must not be empty!");
        }

        byte[] secretData = null;

        try {
            secretData = objectMapper.writeValueAsBytes(dto);

            byte[] encryptedData = AesEncryptionService.encryptSecret(secretData, masterKey);
            String encryptedDataString = Base64.getEncoder().encodeToString(encryptedData);

            KeyDerivationService.wipe(secretData);

            String authHash = AuthHashService.generateAuthHash(masterKey);

            dto.destroy();
            KeyDerivationService.wipe(masterKey);
            masterKey = null;

            SecretRequestDto requestDto = new SecretRequestDto(
                    dto.getTitle(),
                    encryptedDataString
            );
            apiClient.sendSecret(username, authHash, requestDto);
        } finally {
            dto.destroy();
            KeyDerivationService.wipe(masterKey);
            KeyDerivationService.wipe(secretData);
        }
    }

    public void deleteSecret(String username, String title, byte[] masterKey) {
        try {
            String authHash = AuthHashService.generateAuthHash(masterKey);
            KeyDerivationService.wipe(masterKey);

            apiClient.deleteSecret(username, authHash, title);
        } finally {
            KeyDerivationService.wipe(masterKey);
        }
    }

    public List<String> listSecrets(String username, byte[] masterKey) {
        CredentialsValidator.validate(username, masterKey);

        try {
            String authHash = AuthHashService.generateAuthHash(masterKey);

            KeyDerivationService.wipe(masterKey);
            masterKey = null;

            return apiClient.listSecrets(username, authHash);
        } finally {
            KeyDerivationService.wipe(masterKey);
        }
    }

    public SecretDto getDecryptedSecret(String username, String title, byte[] masterKey) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be empty!");
        }

        byte[] decryptedSecret = null;
        byte[] keyForDecryption = masterKey.clone();

        try {
            String authHash = AuthHashService.generateAuthHash(masterKey);

            String encryptedSecret = apiClient.getEncryptedSecret(username, title, authHash);
            byte[] encryptedSecretBytes = Base64.getDecoder().decode(encryptedSecret);

            decryptedSecret = AesEncryptionService.decryptSecret(encryptedSecretBytes, keyForDecryption);

            return objectMapper.readValue(decryptedSecret, SecretDto.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            KeyDerivationService.wipe(keyForDecryption);
            KeyDerivationService.wipe(masterKey);
            KeyDerivationService.wipe(decryptedSecret);
        }
    }
}