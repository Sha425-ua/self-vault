package com.selfvault.crypto;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AesEncryptionServiceTest {

    @Test
    void encryptAndDecrypt_ShouldRestoreOriginalSecret() throws Exception {
        byte[] originalSecret = "Secret123_! ?? ПАроЛЬ".getBytes(StandardCharsets.UTF_8);
        byte[] masterKey = new byte[32];
        new SecureRandom().nextBytes(masterKey);

        byte[] encryptedSecret = AesEncryptionService.encryptSecret(originalSecret.clone(), masterKey);
        byte[] decryptedSecret = AesEncryptionService.decryptSecret(encryptedSecret, masterKey);

        assertArrayEquals(originalSecret, decryptedSecret, "Decrypted password must be equals with original.");
    }

    @Test
    void encryptSecret_ShouldProduceDifferentCipherTextsDueToRandomIV() throws Exception {
        byte[] secret = "Secret123_! ?? ПАроЛЬ".getBytes(StandardCharsets.UTF_8);
        byte[] masterKey = new byte[32];
        new SecureRandom().nextBytes(masterKey);

        byte[] cipher1 = AesEncryptionService.encryptSecret(secret.clone(), masterKey);
        byte[] cipher2 = AesEncryptionService.encryptSecret(secret.clone(), masterKey);

        assertFalse(Arrays.equals(cipher1, cipher2));
    }

    @Test
    void decryptSecret_WithWrongKey_ShouldThrowException() throws Exception {
        byte[] secret = "Secret123_! ??".getBytes(StandardCharsets.UTF_8);

        byte[] correctKey = new byte[32];
        new SecureRandom().nextBytes(correctKey);

        byte[] wrongKey = correctKey.clone();
        wrongKey[0] = 1;

        byte[] cipher = AesEncryptionService.encryptSecret(secret, correctKey);

        assertThrows(Exception.class, () -> {
            AesEncryptionService.decryptSecret(cipher,wrongKey);
        });
    }
}