package com.selfvault.cli.command;

import com.selfvault.client.service.RegisterService;
import com.selfvault.client.util.CredentialsValidator;
import com.selfvault.crypto.KeyDerivationService;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

@Command(name = "register", description = "Регистрация нового пользователя")
public class RegisterCommand implements Runnable {
    private final RegisterService registerService;

    @CommandLine.Option(names = {"-u", "--username"}, required = true, description = "Account username")
    private String username;

    public RegisterCommand(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        System.out.println("New user registration. More details in MkDocs.");

        if (username == null || username.isEmpty()) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
        }

        System.out.print("Enter password: ");
        char[] firstPassword = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        System.out.print("Confirm password: ");
        char[] secondPassword = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        boolean passwordsMatch = Arrays.equals(firstPassword, secondPassword);
        try {
            if (!passwordsMatch) {
                System.err.println("Passwords do not match!");
                return;
            }

            CredentialsValidator.validate(username, firstPassword);
            registerService.register(username, firstPassword);

            System.out.println("User " + username + " successfully registered!");
        } catch (Exception e) {
            System.err.println("Registration failed: " + e.getMessage());
        } finally {
            KeyDerivationService.wipe(firstPassword);
            KeyDerivationService.wipe(secondPassword);
        }
    }
}
