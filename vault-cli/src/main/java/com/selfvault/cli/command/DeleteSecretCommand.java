package com.selfvault.cli.command;

import com.selfvault.cli.client.VaultApiClient;
import com.selfvault.cli.service.SecretService;
import com.selfvault.crypto.KeyDerivationService;
import picocli.CommandLine;

import java.io.Console;
import java.util.Scanner;

@CommandLine.Command(name = "delete", description = "Delete a secret from the vault.")
public class DeleteSecretCommand {
    private final SecretService service;

    @CommandLine.Option(names = {"-u", "--username"}, required = true, description = "Registered username")
    private String username;

    @CommandLine.Option(names = {"-t", "--title"}, required = true, description = "Title of the secret to delete")
    private String title;

    public DeleteSecretCommand(SecretService service) {
        this.service = service;
    }

    public void run() {
        Console console = System.console();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your master password to delete the secret: ");
        char[] password = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        try {
            service.deleteSecret(username, title, password);
            System.out.println("Secret with title: " + title + " successfully deleted for user: " + username);
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            KeyDerivationService.wipe(password);
        }

        System.out.println("Deleting secret with title: " + title + " for user: " + username);
    }
}
