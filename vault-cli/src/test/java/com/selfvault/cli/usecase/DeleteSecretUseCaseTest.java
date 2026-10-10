package com.selfvault.cli.usecase;

import com.selfvault.client.service.AuthenticateService;
import com.selfvault.client.service.SecretService;
import com.selfvault.domain.model.SecretDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteSecretUseCaseTest {

    @Mock
    private AuthenticateService authenticateService;

    @Mock
    private SecretService secretService;

    @InjectMocks
    private DeleteSecretUseCase useCase;

    @Test
    void execute_SuccessfulDelete_CallsServicesAndWipesKey() throws Exception {
        String username = "username";
        String title = "title";
        char[] password = "password".toCharArray();
        byte[] fakeKey = new byte[]{1, 2, 3, 4};

        when(authenticateService.deriveMasterKey(username, password)).thenReturn(fakeKey);

        useCase.execute(username, title, password);

        verify(secretService).deleteSecret(username, title, fakeKey);

        assertArrayEquals(new byte[]{0, 0, 0, 0}, fakeKey, "Master key must cleaned");
    }
}