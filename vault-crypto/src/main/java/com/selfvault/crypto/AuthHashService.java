package com.selfvault.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthHashService {
    private static final byte[] AUTH_LABEL = "self-vault-authentication-key".getBytes(StandardCharsets.UTF_8);
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private AuthHashService() {}

    public static String generateAuthHash(byte[] masterKey) {
        if (masterKey == null || masterKey.length == 0) {
            throw new IllegalArgumentException("Master key cannot be null or empty");
        }

        try {
            Mac hmac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(masterKey, HMAC_ALGORITHM);
            hmac.init(keySpec);

            byte[] hashBytes = hmac.doFinal(AUTH_LABEL);
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("Failed to generate auth hash", e);
        } finally {
            KeyDerivationService.wipe(masterKey);
        }
    }
}
