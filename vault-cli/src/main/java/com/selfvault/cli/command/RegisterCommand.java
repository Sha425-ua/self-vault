package com.selfvault.cli.command;

import com.selfvault.cli.service.RegisterService;
import com.selfvault.crypto.KeyDerivationService;
import picocli.CommandLine.Command;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

@Command(name = "register", description = "Регистрация нового пользователя")
public class RegisterCommand implements Runnable {

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        System.out.println("New user registration. More details in MkDocs.");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        char[] firstPassword = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        System.out.print("Confirm password: ");
        char[] secondPassword = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        if (firstPassword == secondPassword) {
            Arrays.fill(firstPassword, (char) 0);

            try {
                RegisterService.register(username, secondPassword);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
