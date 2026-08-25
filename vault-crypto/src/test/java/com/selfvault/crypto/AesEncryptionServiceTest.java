package com.selfvault.crypto;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AesEncryptionServiceTest {

    @Test
    void encryptAndDecrypt_ShouldRestoreOriginalSecret() throws Exception {
        char[] originalSecret = "Secret123_! ?? ПАроЛЬ".toCharArray();
        byte[] masterKey = new byte[32];
        new SecureRandom().nextBytes(masterKey);

        byte[] encryptedSecret = AesEncryptionService.encryptSecret(originalSecret, masterKey);
        char[] decryptedSecret = AesEncryptionService.decryptSecrets(encryptedSecret, masterKey);

        assertArrayEquals(originalSecret, decryptedSecret, "Decrypted password must be equals with original.");
    }

    @Test
    void encryptSecret_ShouldProduceDifferentCipherTextsDueToRandomIV() throws Exception {
        char[] secret = "Secret123_! ?? ПАроЛЬ".toCharArray();
        byte[] masterKey = new byte[32];
        new SecureRandom().nextBytes(masterKey);

        byte[] cipher1 = AesEncryptionService.encryptSecret(secret.clone(), masterKey);
        byte[] cipher2 = AesEncryptionService.encryptSecret(secret.clone(), masterKey);

        assertFalse(Arrays.equals(cipher1, cipher2));
    }

    @Test
    void decryptSecrets_WithWrongKey_ShouldThrowException() throws Exception {
        char[] secret = "Secret123_! ?? ПАроЛЬ".toCharArray();

        byte[] correctKey = new byte[32];
        new SecureRandom().nextBytes(correctKey);

        byte[] wrongKey = correctKey.clone();
        wrongKey[0] = 1;

        byte[] cipher = AesEncryptionService.encryptSecret(secret, correctKey);

        assertThrows(Exception.class, () -> {
            AesEncryptionService.decryptSecrets(cipher,wrongKey);
        });
    }
}