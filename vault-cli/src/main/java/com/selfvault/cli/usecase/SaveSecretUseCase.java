package com.selfvault.cli.usecase;

import com.selfvault.client.service.AuthenticateService;
import com.selfvault.client.service.SecretService;
import com.selfvault.client.util.CredentialsValidator;
import com.selfvault.crypto.KeyDerivationService;
import com.selfvault.domain.model.SecretDto;

public class SaveSecretUseCase {
    private final AuthenticateService authenticateService;
    private final SecretService secretService;

    public SaveSecretUseCase(AuthenticateService authenticateService, SecretService secretService) {
        this.authenticateService = authenticateService;
        this.secretService = secretService;
    }

    public void execute(String username, char[] password, SecretDto dto) {
        byte[] masterKey = null;

        CredentialsValidator.validate(username, password);

        if (dto.getSecret() == null) {
            throw new IllegalArgumentException("Secret must be not null.");
        }

        try {
            masterKey = authenticateService.deriveMasterKey(username, password);

            secretService.addNewSecret(username, masterKey, dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            KeyDerivationService.wipe(masterKey);
        }
    }
}
