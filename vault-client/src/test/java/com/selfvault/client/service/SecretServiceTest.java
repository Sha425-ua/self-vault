package com.selfvault.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfvault.client.VaultApiClient;
import com.selfvault.crypto.AesEncryptionService;
import com.selfvault.crypto.KeyDerivationService;
import com.selfvault.domain.model.SecretDto;
import com.selfvault.domain.model.SecretRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecretServiceTest {

    @Mock
    private VaultApiClient apiClient;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private SecretService secretService;

    @BeforeEach
    void setUp() {
        secretService = new SecretService(apiClient, objectMapper);
    }

    @Test
    void addNewSecret_ShouldEncrypt() throws Exception {
        char[] secret = "Secret".toCharArray();
        byte[] masterKey = new byte[32];
        String username = "alice";
        String title = "github";
        SecretDto dto = SecretDto.builder(title, secret).build();

        ArgumentCaptor<SecretRequestDto> captor = ArgumentCaptor.forClass(SecretRequestDto.class);

        try (MockedStatic<KeyDerivationService> mockedCrypto = mockStatic(KeyDerivationService.class, CALLS_REAL_METHODS)) {
            secretService.addNewSecret(username, masterKey, dto);

            mockedCrypto.verify(() -> KeyDerivationService.wipe(any(byte[].class)), atLeast(2));
        }

        verify(apiClient, times(1)).sendSecret(eq(username), anyString(), captor.capture());

        SecretRequestDto sendDto = captor.getValue();
        assertEquals(title, sendDto.title());
        assertNotNull(sendDto.encryptedData());
    }

    @Test
    void addNewSecret_ShouldWipeMemory() throws Exception {
        byte[] masterKey = new byte[32];
        String username = "alice";
        String title = "github";
        char[] secret = "Secret12321_".toCharArray();
        SecretDto dto = SecretDto.builder(title, secret).build();

        try (MockedStatic<KeyDerivationService> mockedStatic = mockStatic(KeyDerivationService.class, CALLS_REAL_METHODS)) {
            secretService.addNewSecret(username, masterKey, dto);

            mockedStatic.verify(() -> KeyDerivationService.wipe(masterKey), atLeastOnce());
            assertWiped(masterKey);
        }
    }

    @Test
    void deleteSecret_ShouldDelete() {
        byte[] masterKey = new byte[32];
        String username = "alice";
        String title = "github";

        try (MockedStatic<KeyDerivationService> mockedCrypto = mockStatic(KeyDerivationService.class, CALLS_REAL_METHODS)) {
            secretService.deleteSecret(username, title, masterKey);

            mockedCrypto.verify(() -> KeyDerivationService.wipe(masterKey), atLeastOnce());
        }

        verify(apiClient, times(1)).deleteSecret(eq(username), anyString(), eq(title));
        assertWiped(masterKey);
    }

    @Test
    void deleteSecret_ShouldWipeMemory() {
        byte[] masterKey = new byte[32];
        masterKey[0] = 42;
        String username = "alice";
        String title = "github";

        try (MockedStatic<KeyDerivationService> mockedStatic = mockStatic(KeyDerivationService.class, CALLS_REAL_METHODS)) {
            secretService.deleteSecret(username, title, masterKey);

            mockedStatic.verify(() -> KeyDerivationService.wipe(masterKey), atLeastOnce());
        }
        assertWiped(masterKey);
    }

    @Test
    @DisplayName("Should send request to Vault API")
    void listSecrets_ShouldSendRequestToVaultApiClient() {
        byte[] masterKey = new byte[32];
        String username = "alice";
        List<String> expectedList = List.of("github", "google", "card");

        when(apiClient.listSecrets(eq(username), anyString())).thenReturn(expectedList);

        List<String> listTitle = secretService.listSecrets(username, masterKey);

        verify(apiClient, times(1)).listSecrets(eq(username), anyString());
        assertEquals(expectedList, listTitle);
        assertWiped(masterKey);
    }

    @Test
    void listSecrets_ShouldWipeMemory() {
        byte[] masterKey = new byte[32];
        masterKey[0] = 7;
        String username = "alice";

        when(apiClient.listSecrets(eq(username), anyString())).thenReturn(List.of());

        try (MockedStatic<KeyDerivationService> mockedStatic = mockStatic(KeyDerivationService.class, CALLS_REAL_METHODS)) {
            secretService.listSecrets(username, masterKey);

            mockedStatic.verify(() -> KeyDerivationService.wipe(masterKey), atLeastOnce());
        }
        assertWiped(masterKey);
    }

    @Test
    void getDecryptedSecret_ShouldDecryptSecretAndWipeMemory() throws Exception {
        byte[] masterKey = new byte[32];
        masterKey[0] = 9;
        byte[] masterKeyCopy = masterKey.clone();

        String username = "alice";
        String title = "github";
        char[] secret = "Secret12321_".toCharArray();
        SecretDto originalDto = SecretDto.builder(title, secret).build();

        byte[] serializedDto = objectMapper.writeValueAsBytes(originalDto);
        byte[] encryptedSecretBytes = AesEncryptionService.encryptSecret(serializedDto, masterKeyCopy);
        String encryptedSecret = Base64.getEncoder().encodeToString(encryptedSecretBytes);

        when(apiClient.getEncryptedSecret(eq(username), eq(title), anyString())).thenReturn(encryptedSecret);

        SecretDto decryptedDto;
        try (MockedStatic<KeyDerivationService> mockStatic = mockStatic(KeyDerivationService.class, CALLS_REAL_METHODS)) {
            decryptedDto = secretService.getDecryptedSecret(username, title, masterKey);

            mockStatic.verify(() -> KeyDerivationService.wipe(masterKey), atLeastOnce());
        }

        assertNotNull(decryptedDto);
        assertEquals(title, decryptedDto.getTitle());
        assertArrayEquals(secret, decryptedDto.getSecret());
        assertWiped(masterKey);
    }

    private void assertWiped(byte[] array) {
        byte[] emptyArray = new byte[array.length];
        assertArrayEquals(emptyArray, array);
    }
}