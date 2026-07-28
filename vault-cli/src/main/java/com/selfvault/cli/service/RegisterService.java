package com.selfvault.cli.service;

import com.selfvault.cli.client.VaultApiClient;
import com.selfvault.crypto.AesEncryptionService;
import com.selfvault.crypto.AuthHashService;
import com.selfvault.crypto.KeyDerivationService;
import com.selfvault.crypto.utils.TypeConverter;
import com.selfvault.domain.model.RegisterRequestDto;

import java.util.Arrays;

public class RegisterService {
    public static void register(String username, char[] masterPassword) throws Exception {
        byte[] salt = KeyDerivationService.generateSalt();
        byte[] masterKey = KeyDerivationService.deriveKey(masterPassword, salt);

        String authHash = AuthHashService.generateAuthHash(masterKey);

        KeyDerivationService.wipe(masterPassword);

        RegisterRequestDto dto = new RegisterRequestDto(
                username,
                authHash,
                Arrays.toString(TypeConverter.byteArrayToCharArray(salt))
        );

        VaultApiClient vaultApiClient = new VaultApiClient("http://192.168.1.50:8085");
    }
}
