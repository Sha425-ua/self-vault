package com.selfvault.cli.usecase;

import com.selfvault.client.service.AuthenticateService;
import com.selfvault.client.service.SecretService;
import com.selfvault.client.util.CredentialsValidator;
import com.selfvault.crypto.KeyDerivationService;

import java.util.List;

public class ListUseCase {
    private final AuthenticateService authenticateService;
    private final SecretService secretService;

    public ListUseCase(AuthenticateService authenticateService, SecretService secretService) {
        this.authenticateService = authenticateService;
        this.secretService = secretService;
    }

    public List<String> execute(String username, char[] password) {

        byte[] masterKey = null;

        CredentialsValidator.validate(username, password);

        try {
            masterKey = authenticateService.deriveMasterKey(username, password);

            return secretService.listSecrets(username, masterKey);
        } finally {
            KeyDerivationService.wipe(masterKey);
        }
    }
}