package com.selfvault.cli.usecase;

import com.selfvault.client.service.AuthenticateService;
import com.selfvault.client.service.SecretService;
import com.selfvault.domain.model.SecretDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveSecretUseCaseTest {

    @Mock
    private AuthenticateService authenticateService;

    @Mock
    private SecretService secretService;

    @InjectMocks
    private SaveSecretUseCase useCase;

    @Test
    void execute_SuccessfulSave_CallsServicesAndWipesKey() throws Exception {
        String username = "username";
        char[] password = "password".toCharArray();
        SecretDto dto = SecretDto.builder("title", "secret".toCharArray())
                .build();

        byte[] fakeMasterKey = new byte[]{1, 2, 3, 4};
        when(authenticateService.deriveMasterKey(username, password)).thenReturn(fakeMasterKey);

        useCase.execute(username, password, dto);

        verify(secretService).addNewSecret(username, fakeMasterKey, dto);

        assertArrayEquals(new byte[]{0, 0, 0, 0}, fakeMasterKey, "Master key must cleaned");
    }
}