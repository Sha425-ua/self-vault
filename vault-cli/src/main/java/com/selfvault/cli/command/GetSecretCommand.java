package com.selfvault.cli.command;

import com.selfvault.cli.usecase.GetSecretUseCase;
import com.selfvault.client.service.SecretService;
import com.selfvault.crypto.KeyDerivationService;
import com.selfvault.domain.model.SecretDto;
import picocli.CommandLine;

import java.io.Console;
import java.util.Scanner;

@CommandLine.Command(name = "get", description = "Get secret decrypted from the vault")
public class GetSecretCommand implements Runnable {
    @CommandLine.Option(names = {"-u", "--username"}, required = true, description = "Registered username")
    private String username;

    @CommandLine.Option(names = {"-t", "--title"}, required = true, description = "Title of the secret to show")
    private String title;

    private final GetSecretUseCase getSecretUseCase;

    public GetSecretCommand(GetSecretUseCase getSecretUseCase) {
        this.getSecretUseCase = getSecretUseCase;
    }

    @Override
    public void run() {
        Console console = System.console();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your master password to delete the secret: ");
        char[] masterPassword = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        SecretDto dto = null;

        try {
            dto = getSecretUseCase.execute(username, title, masterPassword);
            renderSecretCard(username, title, dto);

        } catch (Exception e) {
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            KeyDerivationService.wipe(masterPassword);
            if (dto != null) {
                dto.destroy();
            }
        }
    }

    private void renderSecretCard(String username, String title, SecretDto dto) {
        int totalWidth = 48;

        String header = "┌── [ Secret: " + title + " ] ";
        int dashesCount = Math.max(0, totalWidth - header.length() - 1);
        System.out.println(header + "─".repeat(dashesCount) + "┐");

        printRow("User:", username);
        printRow("Title:", title);
        System.out.println("├" + "─".repeat(totalWidth - 2) + "┤");
        printRow("Login:", dto.getLogin());
        printPasswordRow("Password:", dto.getSecret());
        printRow("Note:", dto.getNote());

        System.out.println("└" + "─".repeat(totalWidth - 2) + "┘");
    }

    private void printRow(String label, String value) {
        String displayValue = (value != null && !value.isBlank()) ? value : "-";
        System.out.printf("│ %-10s %-33s │%n", label, displayValue);
    }

    private void printPasswordRow(String label, char[] secret) {
        int padding = Math.max(0, 33 - (secret != null ? secret.length : 1));
        System.out.print("│ " + String.format("%-10s ", label));
        if (secret != null) {
            System.out.print(secret);
        } else {
            System.out.print("-");
        }
        System.out.println(" ".repeat(padding) + "│");
    }
}