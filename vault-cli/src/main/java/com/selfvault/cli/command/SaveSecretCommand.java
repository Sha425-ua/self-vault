package com.selfvault.cli.command;

import com.selfvault.cli.usecase.SaveSecretUseCase;
import com.selfvault.crypto.KeyDerivationService;
import com.selfvault.domain.model.SecretDto;
import picocli.CommandLine;
import java.io.Console;
import java.util.Scanner;


@CommandLine.Command(name = "add", description = "Add new secret to storage")
@SuppressWarnings("unused")
public class SaveSecretCommand implements Runnable {

    private final SaveSecretUseCase useCase;

    @CommandLine.Option(names = {"-u", "--username"}, required = true, description = "Registered username")
    private String username;

    @CommandLine.Option(names = {"-t", "--title"}, required = true, description = "Secret title (e.g., GitHub)")
    private String title;

    @CommandLine.Option(names = {"-l", "--login"}, description = "Login from account")
    private String login;

    @CommandLine.Option(names = {"-n", "--note"}, description = "Your note")
    private String note;

    public SaveSecretCommand(SaveSecretUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void run() {
        Console console = System.console();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your master password to encrypt the secret: ");
        char[] masterPassword = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        System.out.print("Please enter your secret");
        char[] secretText = (console != null)
                ? console.readPassword()
                : scanner.nextLine().toCharArray();

        SecretDto dto = null;

        try {
            dto = SecretDto.builder(title, secretText)
                    .addNote(note)
                    .addLogin(login)
                    .build();

            useCase.execute(username, masterPassword, dto);

            System.out.println("Secret " + title + " successfully added!");
        } catch (Exception e) {
            System.err.println("Error adding secret: " + e.getMessage());
        } finally {
            KeyDerivationService.wipe(masterPassword);
            KeyDerivationService.wipe(secretText);

            if (dto != null) {
                dto.destroy();
            }
        }
    }
}