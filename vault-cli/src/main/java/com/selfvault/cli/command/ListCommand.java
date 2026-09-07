package com.selfvault.cli.command;

import com.selfvault.client.service.SecretService;
import com.selfvault.crypto.KeyDerivationService;
import picocli.CommandLine;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

@CommandLine.Command(name = "list", description = "List all secrets in the vault.")
public class ListCommand implements Runnable {
    @CommandLine.Option(names = {"-u", "--username"}, required = true, description = "Registered username")
    private String username;

    private final SecretService secretService;

    public ListCommand(SecretService secretService) {
        this.secretService = secretService;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        System.out.print("Please enter your master password to delete the secret: ");
        char[] masterPassword = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        try {
            List<String> secrets = secretService.listSecrets(username, masterPassword);

            if (secrets.isEmpty()) {
                System.out.println("No secrets found for user " + username + ".");
            } else {
                String message = String.format("Secrets for user %s (%d found):", username, secrets.size());
                System.out.println(message);

                int i = 1;
                for (String secret : secrets) {
                    String titleMessage = String.format("%d. %s", i++, secret);
                    System.out.println(titleMessage);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            KeyDerivationService.wipe(masterPassword);
        }

    }
}
