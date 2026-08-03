package com.selfvault.cli.command;

import com.selfvault.cli.service.RegisterService;
import com.selfvault.crypto.KeyDerivationService;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

@Command(name = "register", description = "Регистрация нового пользователя")
public class RegisterCommand implements Runnable {
    private final RegisterService registerService;

    public RegisterCommand(RegisterService registerService) {
        this.registerService = registerService;
    }

    @CommandLine.Option(names = {"-u", "--username"}, required = true, description = "Имя пользователя для регистрации")
    private String username;

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

        if (Arrays.equals(firstPassword, secondPassword)) {
            KeyDerivationService.wipe(firstPassword);

            try {
                registerService.register(username, secondPassword);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                KeyDerivationService.wipe(secondPassword);
                KeyDerivationService.wipe(firstPassword);
            }
        }
    }
}
