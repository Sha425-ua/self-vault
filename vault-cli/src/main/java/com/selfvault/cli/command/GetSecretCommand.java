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

        System.out.print("Please enter your master password to delete the secret: ");
        char[] masterPassword = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        char[] secret = null;

        try {
            secret = service.getDecryptedSecret(username, title, masterPassword);

            int totalWidth = 48;

            String header = "┌── [ Secret: " + title + " ] ";
            int dashesCount = Math.max(0, totalWidth - header.length() - 1);
            System.out.println(header + "─".repeat(dashesCount) + "┐");

            System.out.println(String.format("│ User:     %-34s │", username));

            int passwordPadding = Math.max(0, 34 - secret.length);
            System.out.print("│ Password: ");
            System.out.print(secret);
            System.out.println(" ".repeat(passwordPadding) + " │");

            System.out.println("└" + "─".repeat(totalWidth - 2) + "┘");

        } catch (Exception e) {
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            KeyDerivationService.wipe(masterPassword);
            KeyDerivationService.wipe(secret);
        }
    }
}