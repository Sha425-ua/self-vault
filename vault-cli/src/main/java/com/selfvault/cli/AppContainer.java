package com.selfvault.cli;

import com.selfvault.cli.usecase.DeleteSecretUseCase;
import com.selfvault.cli.usecase.GetSecretUseCase;
import com.selfvault.cli.usecase.ListUseCase;
import com.selfvault.cli.usecase.SaveSecretUseCase;
import com.selfvault.client.VaultApiClient;
import com.selfvault.client.service.AuthenticateService;
import com.selfvault.client.service.RegisterService;
import com.selfvault.client.service.SecretService;

public class AppContainer {
    public final VaultApiClient vaultApiClient;
    public final AuthenticateService authenticateService;
    public final SecretService secretService;
    public final RegisterService registerService;

    public final SaveSecretUseCase saveSecretUseCase;
    public final ListUseCase listUseCase;
    public final GetSecretUseCase getSecretUseCase;
    public final DeleteSecretUseCase deleteSecretUseCase;

    public AppContainer() {
        this("http://localhost:8085");
    }

    public AppContainer(String serverUrl) {
        this.vaultApiClient = new VaultApiClient(serverUrl);
        this.authenticateService = new AuthenticateService(vaultApiClient);
        this.secretService = new SecretService(vaultApiClient);
        this.registerService = new RegisterService(vaultApiClient);

        this.saveSecretUseCase = new SaveSecretUseCase(authenticateService, secretService);
        this.listUseCase = new ListUseCase(authenticateService, secretService);
        this.getSecretUseCase = new GetSecretUseCase(authenticateService, secretService);
        this.deleteSecretUseCase = new DeleteSecretUseCase(authenticateService, secretService);
    }
}
