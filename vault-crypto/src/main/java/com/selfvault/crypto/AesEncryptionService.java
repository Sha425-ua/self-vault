package com.selfvault.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

import static com.selfvault.crypto.config.AesGcmConfig.*;
import static com.selfvault.crypto.utils.TypeConverter.*;

public class AesEncryptionService {

    public static byte[] encryptSecret(char[] secretToEncrypt, byte[] masterKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecureRandom random = new SecureRandom();

        byte[] iv = new byte[IV_LENGTH];
        random.nextBytes(iv);

        SecretKey key = new SecretKeySpec(masterKey, "AES");
        byte[] secretBytes = null;

        try {
            secretBytes = charArrayToByteArray(secretToEncrypt);

            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

            byte[] cipherText = cipher.doFinal(secretBytes);

            byte[] result = new byte[IV_LENGTH + cipherText.length];

            System.arraycopy(iv, 0, result, 0, IV_LENGTH);
            System.arraycopy(cipherText, 0, result, IV_LENGTH, cipherText.length);

            return result;
        } finally {
            if (secretBytes != null) {
                Arrays.fill(secretBytes, (byte) 0);
            }
        }
    }

    public static char[] decryptSecrets(byte[] encryptedBytes, byte[] masterKey) throws Exception {
        byte[] iv = Arrays.copyOfRange(encryptedBytes, 0, IV_LENGTH);
        byte[] cipherText = Arrays.copyOfRange(encryptedBytes, IV_LENGTH, encryptedBytes.length);

        SecretKey key = new SecretKeySpec(masterKey, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        byte[] plainBytes = null;
        try {
            plainBytes = cipher.doFinal(cipherText);
            return byteArrayToCharArray(plainBytes);
        } finally {
            if (plainBytes != null) {
                Arrays.fill(plainBytes, (byte) 0);
            }
        }
    }
}
