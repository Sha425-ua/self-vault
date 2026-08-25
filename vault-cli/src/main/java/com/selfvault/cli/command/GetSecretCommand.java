package com.selfvault.cli.command;

import com.selfvault.cli.service.SecretService;
import com.selfvault.crypto.KeyDerivationService;
import picocli.CommandLine;

import java.io.Console;
import java.util.Scanner;

@CommandLine.Command(name = "get", description = "Get secret decrypted from the vault")
public class GetSecretCommand implements Runnable {
    @CommandLine.Option(names = {"-u", "--username"}, required = true, description = "Registered username")
    private String username;

    @CommandLine.Option(names = {"-t", "--title"}, required = true, description = "Title of the secret to show")
    private String title;

    private final SecretService service;

    public GetSecretCommand(SecretService service) {
        this.service = service;
    }

    @Override
    public void run() {
        Console console = System.console();
        Scanner scanner = new Scanner(System.in);

        char[] masterPassword = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        char[] secret = null;

        try {
            secret = service.getDecryptedSecret(username, title, masterPassword);

            System.out.println("┌── [ Secret: " + title + " ] ──────────────────────────┐");
            System.out.println("│ User:     " + String.format("%-36s", username) + " │");
            System.out.print("│ Password: ");
            System.out.print(secret);
            System.out.println();
            System.out.println("└────────────────────────────────────────────────┘");

        } catch (Exception e) {
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            KeyDerivationService.wipe(masterPassword);
            KeyDerivationService.wipe(secret);
        }
    }
}