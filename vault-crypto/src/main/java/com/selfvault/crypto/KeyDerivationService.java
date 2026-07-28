package com.selfvault.crypto;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.security.SecureRandom;
import java.util.Arrays;

public class KeyDerivationService {

    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    private static Argon2Parameters buildParameters(byte[] salt) {
        return new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(3)
                .withMemoryAsKB(65536)
                .withParallelism(4)
                .withSalt(salt)
                .build();
    }

    private static byte[] generateKey(char[] password, Argon2Parameters parameters) {
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);

        byte[] resultKey = new byte[32];
        generator.generateBytes(password, resultKey);

        return resultKey;
    }

    public static byte[] deriveKey(char[] password, byte[] salt) {
        Argon2Parameters parameters = buildParameters(salt);
        return generateKey(password, parameters);
    }

    public static void wipe(byte[] array) {
        if (array != null) {
            Arrays.fill(array, (byte) 0);
        }
    }

    public static void wipe(char[] array) {
        if (array != null) {
            Arrays.fill(array, '\0');
        }
    }
}
