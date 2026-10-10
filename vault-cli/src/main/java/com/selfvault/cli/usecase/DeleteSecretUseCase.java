package com.selfvault.cli.usecase;

import com.selfvault.client.service.AuthenticateService;
import com.selfvault.client.service.SecretService;
import com.selfvault.client.util.CredentialsValidator;
import com.selfvault.crypto.KeyDerivationService;

public class DeleteSecretUseCase {
    private final AuthenticateService authenticateService;
    private final SecretService secretService;

    public DeleteSecretUseCase(AuthenticateService authenticateService, SecretService secretService) {
        this.authenticateService = authenticateService;
        this.secretService = secretService;
    }

    public void execute(String username, String title, char[] password) {

        CredentialsValidator.validate(username, password);

        byte[] masterKey = null;

        try {
            masterKey = authenticateService.deriveMasterKey(username, password);

            secretService.deleteSecret(username, title, masterKey);
        } finally {
            KeyDerivationService.wipe(masterKey);
        }
    }
}
