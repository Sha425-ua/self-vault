package com.selfvault.cli.service;

import com.selfvault.cli.client.VaultApiClient;
import com.selfvault.crypto.AesEncryptionService;
import com.selfvault.crypto.KeyDerivationService;
import com.selfvault.domain.model.SecretRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecretServiceTest {

    @Mock
    private AuthenticateService authenticateService;

    @InjectMocks
    private SecretService secretService;

    @Mock
    private VaultApiClient apiClient;


    @Test
    void addNewSecret_ShouldEncrypt() throws Exception {
        char[] secret = "Secret".toCharArray();
        char[] masterPassword = "Password123_1/>".toCharArray();
        String username = "alice";
        String title = "github";

        when(authenticateService.getUserSalt(username)).thenReturn(new byte[16]);

        ArgumentCaptor<SecretRequestDto> captor = ArgumentCaptor.forClass(SecretRequestDto.class);

        try (MockedStatic<KeyDerivationService> mockedCrypto = mockStatic(KeyDerivationService.class, CALLS_REAL_METHODS)){
            secretService.addNewSecret(username, title, secret, masterPassword);

            mockedCrypto.verify(() -> KeyDerivationService.wipe(any(byte[].class)), atLeast(2));
        }

        verify(apiClient, times(1)).sendSecret(eq(username), anyString(), captor.capture());

        SecretRequestDto sendDto = captor.getValue();
        assertEquals(title, sendDto.title());
        assertNotNull(sendDto.encryptedData());
    }

    @Test
    void addNewSecret_ShouldWipeMemory() throws Exception {
        char[] masterPassword = "Password123_1/>".toCharArray();
        String username = "alice";
        String title = "github";
        char[] secret = "Secret12321_".toCharArray();
        byte[] testMasterKey = new byte[32];

        when(authenticateService.getUserSalt(username)).thenReturn(new byte[16]);

        try (MockedStatic<KeyDerivationService> mockedStatic = mockStatic(KeyDerivationService.class)) {
            mockedStatic.when(() -> KeyDerivationService.deriveKey(any(), any()))
                    .thenReturn(testMasterKey);

            secretService.addNewSecret(username, title, secret, masterPassword);

            mockedStatic.verify(() -> KeyDerivationService.wipe(testMasterKey), times(2));
            mockedStatic.verify(() -> KeyDerivationService.wipe(masterPassword), times(2));
            mockedStatic.verify(() -> KeyDerivationService.wipe(secret), atLeast(2));
        }
    }

    @Test
    void deleteSecret_ShouldDelete() throws Exception{
        char[] masterPassword = "Password123_1/>".toCharArray();
        String username = "alice";
        String title = "github";

        when(authenticateService.getUserSalt(username)).thenReturn(new byte[16]);

        try (MockedStatic<KeyDerivationService> mockedCrypto = mockStatic(KeyDerivationService.class, CALLS_REAL_METHODS)){
            secretService.deleteSecret(username, title, masterPassword);

            mockedCrypto.verify(() -> KeyDerivationService.wipe(any(byte[].class)), atLeast(2));
        }

        verify(apiClient, times(1)).deleteSecret(eq(username), anyString(), eq(title));

        assertWiped(masterPassword);
    }

    @Test
    void deleteSecret_ShouldWipeMemory() throws Exception {
        char[] masterPassword = "Password123_1/>".toCharArray();
        String username = "alice";
        String title = "github";
        byte[] testMasterKey = new byte[32];

        when(authenticateService.getUserSalt(username)).thenReturn(new byte[16]);

        try (MockedStatic<KeyDerivationService> mockedStatic = mockStatic(KeyDerivationService.class)) {
            mockedStatic.when(() -> KeyDerivationService.deriveKey(any(), any()))
                    .thenReturn(testMasterKey);

            secretService.deleteSecret(username, title, masterPassword);

            mockedStatic.verify(() -> KeyDerivationService.wipe(testMasterKey), times(2));
            mockedStatic.verify(() -> KeyDerivationService.wipe(masterPassword), times(2));
        }
    }

    @Test
    @DisplayName("Should send request to Vault API")
    void listSecrets_ShouldSendRequestToVaultApiClient() throws Exception {
        char[] masterPassword = "Password123_1/>".toCharArray();
        String username = "alice";
        List<String> expectedList = List.of("github", "google", "card");

        when(authenticateService.getUserSalt(username)).thenReturn(new byte[16]);
        when(apiClient.listSecrets(eq(username), anyString())).thenReturn(expectedList);

        List<String> listTitle = secretService.listSecrets(username, masterPassword);

        verify(apiClient, times(1)).listSecrets(eq(username), anyString());
        assertEquals(expectedList, listTitle);
        assertWiped(masterPassword);
    }

    @Test
    void listSecrets_ShouldWipeMemory() throws Exception {
        char[] masterPassword = "Password123_1/>".toCharArray();
        String username = "alice";
        byte[] testMasterKey = "Password123_1/>".getBytes(StandardCharsets.UTF_8);

        when(authenticateService.getUserSalt(username)).thenReturn(new byte[16]);

        try (MockedStatic<KeyDerivationService> mockedStatic = mockStatic(KeyDerivationService.class)) {
            mockedStatic.when(() -> KeyDerivationService.deriveKey(any(), any()))
                    .thenReturn(testMasterKey);

            secretService.listSecrets(username, masterPassword);

            mockedStatic.verify(() -> KeyDerivationService.wipe(testMasterKey), times(2));
            mockedStatic.verify(() -> KeyDerivationService.wipe(masterPassword), times(2));
        }
    }

    @Test
    void getDecryptedSecret_ShouldDecryptSecretAndWipeMemory() throws Exception {
        char[] masterPassword = "Password123_1/>".toCharArray();
        String username = "alice";
        String title = "github";
        byte[] testMasterKey = new byte[32];
        char[] secret = "Secret12321_".toCharArray();

        byte[] encryptedSecretBytes = AesEncryptionService.encryptSecret(secret.clone(), testMasterKey);

        String encryptedSecret = Base64.getEncoder().encodeToString(encryptedSecretBytes);

        when(authenticateService.getUserSalt(username)).thenReturn(new byte[16]);
        when(apiClient.getEncryptedSecret(eq(username), eq(title), anyString())).thenReturn(encryptedSecret);

        char[] decryptedSecret;

        try (MockedStatic<KeyDerivationService> mockStatic = mockStatic(KeyDerivationService.class)) {
            mockStatic.when(() -> KeyDerivationService.deriveKey(eq(masterPassword), any(byte[].class)))
                    .thenReturn(testMasterKey);

            decryptedSecret = secretService.getDecryptedSecret(username, title, masterPassword);

            mockStatic.verify(() -> KeyDerivationService.wipe(testMasterKey), times(1));
            mockStatic.verify(() -> KeyDerivationService.wipe(masterPassword), times(2));
        }

        assertArrayEquals(secret, decryptedSecret);
    }

    private void assertWiped(char[] array) {
        char[] emptyArray = new char[array.length];
        assertArrayEquals(emptyArray, array);
    }
}