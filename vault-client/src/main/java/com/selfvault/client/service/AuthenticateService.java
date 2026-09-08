package com.selfvault.client.service;

import com.selfvault.client.VaultApiClient;
import com.selfvault.crypto.AuthHashService;
import com.selfvault.crypto.KeyDerivationService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class AuthenticateService {
    private final VaultApiClient apiClient;

    public AuthenticateService(VaultApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public byte[] getUserSalt(String username) {
        return Base64.getDecoder().decode(apiClient.getSalt(username).salt());
    }

    public void login(String username, char[] masterPassword) {
        byte[] salt;
        byte[] masterKey = null;

        try {
            salt = getUserSalt(username);
            masterKey = KeyDerivationService.deriveKey(masterPassword, salt);
            KeyDerivationService.wipe(masterPassword);

            String authHash = AuthHashService.generateAuthHash(masterKey);
            KeyDerivationService.wipe(masterKey);

            apiClient.login(username, authHash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            KeyDerivationService.wipe(masterKey);
            KeyDerivationService.wipe(masterPassword);
        }
    }

    public static boolean verifyAuthHash(String expectedHash, String actualHash) {
        if (expectedHash == null || actualHash == null) {
            return false;
        }
        byte[] expectedBytes = expectedHash.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = actualHash.getBytes(StandardCharsets.UTF_8);

        return MessageDigest.isEqual(expectedBytes, actualBytes);
    }
}
