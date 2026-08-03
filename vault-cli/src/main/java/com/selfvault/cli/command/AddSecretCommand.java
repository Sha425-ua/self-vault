package com.selfvault.cli.command;

import com.selfvault.cli.service.SecretService;
import picocli.CommandLine;
import java.io.Console;
import java.util.Scanner;


@CommandLine.Command(name = "add", description = "Add new secret to storage")
@SuppressWarnings("unused")
public class AddSecretCommand implements Runnable {

    private final SecretService service;

    @CommandLine.Option(names = {"-u", "--username"}, required = true, description = "Registered username")
    private String username;

    @CommandLine.Option(names = {"-t", "--title"}, required = true, description = "Secret title (e.g., GitHub)")
    private String title;

    @CommandLine.Option(names = {"-s", "--secret"}, required = true, description = "Secret contents")
    private char[] secretText;

    public AddSecretCommand(SecretService service) {
        this.service = service;
    }

    @Override
    public void run() {
        Console console = System.console();
        Scanner scanner = new Scanner(System.in);


        System.out.print("Please enter your master password to encrypt the secret: ");
        char[] masterPassword = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        try {
            service.addNewSecret(username, title, secretText, masterPassword);

            System.out.println("Secret " + title + " successfully added!");
        } catch (Exception e) {
            System.out.println("Error adding secret: " + e.getMessage());
        }
    }
}