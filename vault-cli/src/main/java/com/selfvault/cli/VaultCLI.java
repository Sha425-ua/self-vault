package com.selfvault.cli;

import com.selfvault.cli.command.*;
import com.selfvault.cli.usecase.DeleteSecretUseCase;
import com.selfvault.cli.usecase.GetSecretUseCase;
import com.selfvault.cli.usecase.ListUseCase;
import com.selfvault.cli.usecase.SaveSecretUseCase;
import picocli.CommandLine;

@CommandLine.Command(
        name = "self-vault",
        mixinStandardHelpOptions = true,
        version = "self-vault 1.0.1",
        description = "Self-Vault secure storage of secrets and passwords."
)
public class VaultCLI implements Runnable {

    public static void main(String[] args) {
        AppContainer container = new AppContainer();

        CommandLine commandLine = new CommandLine(new VaultCLI())
                .addSubcommand("register", new RegisterCommand(container.registerService))
                .addSubcommand("add", new SaveSecretCommand(container.saveSecretUseCase))
                .addSubcommand("delete", new DeleteSecretCommand(container.deleteSecretUseCase))
                .addSubcommand("list", new ListCommand(container.listUseCase))
                .addSubcommand("get", new GetSecretCommand(container.getSecretUseCase));

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