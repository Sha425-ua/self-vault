package com.selfvault.cli;

import com.selfvault.cli.command.*;
import com.selfvault.client.service.AuthenticateService;
import com.selfvault.client.service.RegisterService;
import com.selfvault.client.service.SecretService;
import picocli.CommandLine;

import com.selfvault.client.*;

@CommandLine.Command(
        name = "self-vault",
        mixinStandardHelpOptions = true,
        version = "self-vault 1.0.1",
        description = "Self-Vault secure storage of secrets and passwords."
)
public class VaultCLI implements Runnable {
    public static void main(String[] args) {
        VaultApiClient apiClient = new VaultApiClient("http://localhost:8085");

        AuthenticateService authenticateService = new AuthenticateService(apiClient);
        SecretService secretService = new SecretService(apiClient, authenticateService);
        RegisterService registerService = new RegisterService(apiClient);

        CommandLine commandLine = new CommandLine(new VaultCLI())
                .addSubcommand("register", new RegisterCommand(registerService))
                .addSubcommand("add", new AddSecretCommand(secretService))
                .addSubcommand("delete", new DeleteSecretCommand(secretService))
                .addSubcommand("list", new ListCommand(secretService))
                .addSubcommand("get", new GetSecretCommand(secretService));

        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("""
                   _____      ______    _    __            ____\s
                  / ___/___  / / __/   | |  / /___ ___  __/ / /_
                  \\__ \\/ _ \\/ / /______| | / / __ `/ / / / / __/
                 ___/ /  __/ / __/_____/ |/ / /_/ / /_/ / / /_ \s
                /____/\\___/_/_/        |___/\\__,_/\\__,_/_/\\__/ \s
                """);
        System.out.println("Use '--help' to see a list of available commands.");
    }
}