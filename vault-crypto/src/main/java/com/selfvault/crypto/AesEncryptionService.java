package com.selfvault.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

import static com.selfvault.crypto.config.AesGcmConfig.*;

public class AesEncryptionService {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static byte[] encryptSecret(byte[] secretToEncrypt, byte[] masterKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        byte[] iv = new byte[IV_LENGTH];
        RANDOM.nextBytes(iv);

        SecretKey key = new SecretKeySpec(masterKey, "AES");

        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

            byte[] cipherText = cipher.doFinal(secretToEncrypt);

            byte[] result = new byte[IV_LENGTH + cipherText.length];

            System.arraycopy(iv, 0, result, 0, IV_LENGTH);
            System.arraycopy(cipherText, 0, result, IV_LENGTH, cipherText.length);

            return result;
        } finally {
            KeyDerivationService.wipe(secretToEncrypt);
        }
    }

    public static byte[] decryptSecret(byte[] encryptedBytes, byte[] masterKey) throws Exception {

        if (encryptedBytes == null || encryptedBytes.length <= IV_LENGTH) {
            throw new IllegalArgumentException("Encrypted data is invalid or too short!");
        }

        try {
            byte[] iv = Arrays.copyOfRange(encryptedBytes, 0, IV_LENGTH);
            byte[] cipherText = Arrays.copyOfRange(encryptedBytes, IV_LENGTH, encryptedBytes.length);

            SecretKey key = new SecretKeySpec(masterKey, "AES");

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

            return cipher.doFinal(cipherText);
        } finally {
            KeyDerivationService.wipe(encryptedBytes);
        }
    }
}
