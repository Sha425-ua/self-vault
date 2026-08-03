package com.selfvault.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyDerivationServiceTest {

    @Test
    void generateSalt() {
    }

    @Test
    void deriveKey_ShouldReturn32BytesKey() {
        char[] password = "Password123".toCharArray();
        byte[] salt = KeyDerivationService.generateSalt();

        byte[] masterKey = KeyDerivationService.deriveKey(password, salt);

        assertNotNull(masterKey, "Key cannot be null!");
        assertEquals(32, masterKey.length, "Length need to be 32 byte.");
    }

    @Test
    void deriveKey_ShouldGenerateSameKeyForSomeInput() {
        byte[] salt = KeyDerivationService.generateSalt();

        byte[] key1 = KeyDerivationService.deriveKey("Password123".toCharArray(), salt);
        byte[] key2 = KeyDerivationService.deriveKey("Password123".toCharArray(), salt);

        assertArrayEquals(key1, key2, "For identical password keys must be equals!");
    }

    @Test
    void wipe_ShouldFillArrayWithZeros() {
        char[] password = "Password123".toCharArray();
        KeyDerivationService.wipe(password);
        for (char c : password) {
            assertEquals('\0', c, "Array after wipe must be filled with zeros");
        }
    }
}