package com.selfvault.client.util;

public final class CredentialsValidator {
    private CredentialsValidator() {}

    public static void validate(String username, char[] password) {
        if (username == null || username.isBlank() || password == null || password.length == 0) {
            throw new IllegalArgumentException("Username and master password must not be empty!");
        }
    }
    public static void validate(String username, byte[] masterKey) {
        if (username == null || username.isBlank() || masterKey == null || masterKey.length == 0) {
            throw new IllegalArgumentException("Username and master key must not be empty!");
        }
    }
}
