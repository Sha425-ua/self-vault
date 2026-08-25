package com.selfvault.server.service;

import com.selfvault.domain.exception.UserNotFoundException;
import com.selfvault.domain.model.SecretRequestDto;
import com.selfvault.server.entity.SecretEntity;
import com.selfvault.server.repository.SecretRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecretServiceTest {

    @Mock
    private SecretRepository repository;

    @InjectMocks
    private SecretService service;

    @Test
    void saveSecret_ShouldMapFieldsAndSaveEntity() {
        String username = "alice";
        SecretRequestDto dto = new SecretRequestDto("title", "encryptedData");
        ArgumentCaptor<SecretEntity> captor = ArgumentCaptor.forClass(SecretEntity.class);

        service.saveSecret(username, dto);

        verify(repository, times(1)).save(captor.capture());

        SecretEntity entity = captor.getValue();
        assertEquals(dto.title(), entity.getTitle());
        assertEquals(dto.encryptedData(), entity.getEncryptedData());
        assertEquals(username, entity.getUsername());
    }

    @Test
    void deleteSecret_WhenUserExists_ShouldDeleteSecret() {
        String username = "alice";
        String title = "github";

        when(repository.existsByUsernameAndTitle(username, title)).thenReturn(true);

        service.deleteSecret(username, title);

        verify(repository, times(1)).deleteByUsernameAndTitle(username, title);
    }

    @Test
    void deleteSecret_WhenSecretDoesNotExist_ShouldThrowException() {
        String username = "alice";
        String title = "github";

        when(repository.existsByUsernameAndTitle(username, title)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.deleteSecret(username, title));

        verify(repository, never()).deleteByUsernameAndTitle(anyString(), anyString());
    }

    @Test
    void getSecretTitles_WhenSecretsExist_ShouldReturnList() {
        when(repository.getTitlesByUsername("alice")).thenReturn(List.of("github", "google"));

        List<String> titles = service.getSecretTitles("alice");

        assertEquals(2, titles.size());
        assertEquals(List.of("github", "google"), titles);
    }

    @Test
    void getSecretTitles_WhenNoSecrets_ShouldReturnEmptyList() {
        when(repository.getTitlesByUsername("alice")).thenReturn(Collections.emptyList());

        List<String> titles = service.getSecretTitles("alice");

        assertTrue(titles.isEmpty());
    }

    @Test
    void getEncryptedData_WhenSecretExists_ShouldReturnEncryptedString() {
        String username = "alice";
        String title = "github";
        String expectedPayload = "encryptedDataPayloadBase64==";

        when(repository.getEncryptedData(username, title)).thenReturn(Optional.of(expectedPayload));

        String actualPayload = service.getEncryptedData(username, title);

        assertEquals(expectedPayload, actualPayload);
    }

    @Test
    void getEncryptedData_WhenSecretNotFound_ShouldThrowUserNotFoundException() {
        String username = "alice";
        String title = "secret";

        when(repository.getEncryptedData(username, title)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getEncryptedData(username, title));
    }
}