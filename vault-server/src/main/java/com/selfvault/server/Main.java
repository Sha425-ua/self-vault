package com.selfvault.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Arrays;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        Path inputPath = Path.of("test.txt");
        Files.writeString(inputPath, "Привет! Это секретный текст для проверки шифрования.");

        byte[] fileBytes = Files.readAllBytes(Path.of("test.txt"));

        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] cipherText = cipher.doFinal(fileBytes);
        byte[] resultBytes = new byte[iv.length + cipherText.length];

        System.arraycopy(iv, 0, resultBytes, 0, iv.length);
        System.arraycopy(cipherText, 0, resultBytes, iv.length, cipherText.length);

        System.out.println(Arrays.toString(fileBytes));
        System.out.println(Arrays.toString(resultBytes));
        Files.write(Paths.get("test.enc"), resultBytes);

        Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] fileBytes1 = Files.readAllBytes(Path.of("test.enc"));
        byte[] iv1 = Arrays.copyOfRange(fileBytes1, 0, 16);
        byte[] cipherText1 = Arrays.copyOfRange(fileBytes1, 16, fileBytes1.length);

        IvParameterSpec ivSpec1 = new IvParameterSpec(iv1);
        
        cipher1.init(Cipher.DECRYPT_MODE, secretKey, ivSpec1);

        byte[] cipherText1Enc = cipher1.doFinal(cipherText1);
        Files.write(Paths.get("test.dec"), cipherText1Enc);
        System.out.println(Arrays.toString(cipherText1Enc));

        SpringApplication.run(Main.class, args);
    }
}
