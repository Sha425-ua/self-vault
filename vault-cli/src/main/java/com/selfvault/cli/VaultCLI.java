package com.selfvault.cli;

import com.selfvault.cli.command.RegisterCommand;
import picocli.CommandLine;

@CommandLine.Command(
        name = "self-vault",
        mixinStandardHelpOptions = true,
        version = "self-vault 1.0.0",
        description = "Self-Vault secure storage of secrets and passwords.",
        subcommands = {
                RegisterCommand.class
        }
)
public class VaultCLI implements Runnable {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new VaultCLI()).execute(args);
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