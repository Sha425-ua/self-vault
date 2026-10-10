package com.selfvault.cli.usecase;

import com.selfvault.client.service.AuthenticateService;
import com.selfvault.client.service.SecretService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListUseCaseTest {

    @Mock
    private AuthenticateService authenticateService;

    @Mock
    private SecretService secretService;

    @InjectMocks
    private ListUseCase useCase;

    @Test
    void execute_SuccessfulList_ReturnsSecretsAndWipesKey() {
        String username = "username";
        char[] password = "password".toCharArray();
        byte[] fakeMasterKey = new byte[]{1, 2, 3, 4};
        List<String> expectedSecrets = List.of("GitHub", "Google", "Bank");

        when(authenticateService.deriveMasterKey(username, password)).thenReturn(fakeMasterKey);
        when(secretService.listSecrets(username, fakeMasterKey)).thenReturn(expectedSecrets);

        List<String> actualSecrets = useCase.execute(username, password);

        assertEquals(expectedSecrets, actualSecrets);
        verify(secretService).listSecrets(username, fakeMasterKey);
        assertArrayEquals(new byte[]{0, 0, 0, 0}, fakeMasterKey, "Master key must be wiped");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t\n"})
    void execute_InvalidUsername_ThrowsExceptionAndNeverCallsServices(String invalidUsername) {
        char[] password = "password".toCharArray();

        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(invalidUsername, password)
        );

        verifyNoInteractions(authenticateService, secretService);
    }
}
