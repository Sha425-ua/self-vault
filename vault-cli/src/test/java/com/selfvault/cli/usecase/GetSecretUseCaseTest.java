package com.selfvault.cli.usecase;

import com.selfvault.client.service.AuthenticateService;
import com.selfvault.client.service.SecretService;
import com.selfvault.domain.model.SecretDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetSecretUseCaseTest {

    @Mock
    private AuthenticateService authenticateService;

    @Mock
    private SecretService secretService;

    @InjectMocks
    private GetSecretUseCase useCase;

    @Test
    void execute_SuccessfulGet_ReturnsSecretDtoAndWipesKey() {
        String username = "username";
        String title = "GitHub";
        char[] password = "password".toCharArray();
        byte[] fakeMasterKey = new byte[]{1, 2, 3, 4};

        SecretDto expectedDto = SecretDto.builder(title, "my_secret_token".toCharArray())
                .addLogin("alice")
                .addNote("work account")
                .build();

        when(authenticateService.deriveMasterKey(username, password)).thenReturn(fakeMasterKey);
        when(secretService.getDecryptedSecret(username, title, fakeMasterKey)).thenReturn(expectedDto);

        SecretDto actualDto = useCase.execute(username, title, password);

        assertSame(expectedDto, actualDto);
        verify(secretService).getDecryptedSecret(username, title, fakeMasterKey);
        assertArrayEquals(new byte[]{0, 0, 0, 0}, fakeMasterKey, "Master key must be wiped");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void execute_InvalidUsername_ThrowsExceptionAndNeverCallsServices(String invalidUsername) {
        String title = "GitHub";
        char[] password = "password".toCharArray();

        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(invalidUsername, title, password)
        );

        verifyNoInteractions(authenticateService, secretService);
    }
}
