package com.selfvault.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthHashService {
    private static final String AUTH_LABEL = "self-vault-authentication-key";

    public static String generateAuthHash(byte[] masterKey) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(masterKey, "HmacSHA256");
        hmac.init(keySpec);

        byte[] hashBytes = hmac.doFinal(AUTH_LABEL.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}
