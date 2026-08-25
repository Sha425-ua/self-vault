package com.selfvault.crypto;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class AuthHashServiceTest {
    @Test
    void generateAuthHash_ShouldBeDeterministicForSameKey() throws Exception {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);

        String hash1 = AuthHashService.generateAuthHash(key.clone());
        String hash2 = AuthHashService.generateAuthHash(key.clone());

        assertEquals(hash1, hash2);
    }

    @Test
    void generateAuthHash_ShouldGenerateDifferentHashesForDifferentKeys() throws Exception {
        byte[] key1 = new byte[32];
        byte[] key2 = new byte[32];
        key1[0] = 1;
        key2[0] = 2;

        String hash1 = AuthHashService.generateAuthHash(key1);
        String hash2 = AuthHashService.generateAuthHash(key2);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void generateAuthHash_ShouldReturnValidBase64() throws Exception {
        byte[] key = new byte[32];
        String hash = AuthHashService.generateAuthHash(key);

        assertNotNull(hash);
        assertFalse(hash.isBlank());

        assertDoesNotThrow(() -> Base64.getDecoder().decode(hash));
    }
}