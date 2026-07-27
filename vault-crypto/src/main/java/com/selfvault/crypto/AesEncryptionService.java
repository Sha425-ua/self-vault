package com.selfvault.crypto;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import static com.selfvault.crypto.config.AesGcmConfig.*;

public class AesEncryptionService {

    public static byte[] encryptSecret(char[] secretsToEncrypt, char[] masterPassword) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecureRandom random = new SecureRandom();

        byte[] iv = new byte[IV_LENGTH];
        random.nextBytes(iv);

        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        SecretKey key = deriveKey(masterPassword, salt);
        byte[] secretBytes = null;

        try {
            secretBytes = charArrayToByteArray(secretsToEncrypt);

            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

            byte[] cipherText = cipher.doFinal(secretBytes);

            byte[] result = new byte[SALT_LENGTH + IV_LENGTH + cipherText.length];

            System.arraycopy(salt, 0, result, 0, SALT_LENGTH);
            System.arraycopy(iv, 0, result, SALT_LENGTH, IV_LENGTH);
            System.arraycopy(cipherText, 0, result, SALT_LENGTH + IV_LENGTH, cipherText.length);

            return result;
        } finally {
            if (secretBytes != null) {
                Arrays.fill(secretBytes, (byte) 0);
            }
        }
    }

    public static char[] decryptSecrets(byte[] encryptedBytes, char[] massterPassword) throws Exception {
       byte[] salt = Arrays.copyOfRange(encryptedBytes, 0, SALT_LENGTH);
       byte[] iv = Arrays.copyOfRange(encryptedBytes, SALT_LENGTH, SALT_LENGTH + IV_LENGTH);
       byte[] cipherText = Arrays.copyOfRange(encryptedBytes, SALT_LENGTH + IV_LENGTH, encryptedBytes.length);

       SecretKey key = deriveKey(massterPassword, salt);

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

    private static SecretKey deriveKey(char[] password, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(
                password,
                salt,
                ITERATIONS,
                256
        );
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        try {
            return new SecretKeySpec(keyBytes, "AES");
        } finally {
            if (keyBytes != null) {
                Arrays.fill(keyBytes, (byte) 0);
            }
        }

    }

    private static byte[] charArrayToByteArray(char[] chars) {
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(chars));
        byte[] bytes = Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }


    private static char[] byteArrayToCharArray(byte[] bytes) {
        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(bytes));
        char[] chars = Arrays.copyOf(charBuffer.array(), charBuffer.limit());
        Arrays.fill(charBuffer.array(), '\0');
        return chars;
    }
}
