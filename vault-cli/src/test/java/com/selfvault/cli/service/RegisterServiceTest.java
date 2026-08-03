package com.selfvault.cli.service;

import com.selfvault.cli.client.VaultApiClient;
import com.selfvault.domain.model.RegisterRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private VaultApiClient apiClient;

    @InjectMocks
    private RegisterService registerService;

    @Test
    void register_ShouldComputeAuthHashAndSendDtoToNetwork() throws Exception {
        String username = "Username";
        char[] password = "Password123".toCharArray();

        ArgumentCaptor<RegisterRequestDto> captor = ArgumentCaptor.forClass(RegisterRequestDto.class);

        registerService.register(username, password);

        verify(apiClient).register(captor.capture());

        RegisterRequestDto dto = captor.getValue();
        assertEquals("Username", dto.username());
        assertNotNull(dto.authHash(), "authHash shouldn't be null!");
        assertNotNull(dto.salt(), "salt shouldn't be null!");

        for (char c : password) {
            assertEquals('\0', c, "Password chars must be zeros.");
        }
    }
}