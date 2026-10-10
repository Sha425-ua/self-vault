package com.selfvault.cli.usecase;

import com.selfvault.client.service.AuthenticateService;
import com.selfvault.client.service.SecretService;
import com.selfvault.client.util.CredentialsValidator;
import com.selfvault.crypto.KeyDerivationService;
import com.selfvault.domain.model.SecretDto;

public class GetSecretUseCase {
    private final AuthenticateService authenticateService;
    private final SecretService secretService;

    public GetSecretUseCase(AuthenticateService authenticateService, SecretService secretService) {
        this.authenticateService = authenticateService;
        this.secretService = secretService;
    }

    public SecretDto execute(String username, String title, char[] password) {
        byte[] masterKey = null;

        CredentialsValidator.validate(username, password);

        try {
            masterKey = authenticateService.deriveMasterKey(username, password);

            return secretService.getDecryptedSecret(username, title, masterKey);
        } finally {
            KeyDerivationService.wipe(masterKey);
        }
    }
}
