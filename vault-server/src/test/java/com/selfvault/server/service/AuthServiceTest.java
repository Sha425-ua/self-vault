package com.selfvault.server.service;

import com.selfvault.domain.exception.AuthException;
import com.selfvault.domain.exception.UserNotFoundException;
import com.selfvault.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private AuthService authService;

    @Test
    void authUser_WhenValidCredentials_ShouldNotThrowException() {
        when(repository.existsByUsernameAndAuthHash("alice", "validHash")).thenReturn(true);

        assertDoesNotThrow(() -> authService.authUser("alice", "validHash"));
    }

    @Test
    void authUser_WhenInvalidCredentials_ShouldThrowAuthException() {
        when(repository.existsByUsernameAndAuthHash("alice", "validHash")).thenReturn(false);

        assertThrows(AuthException.class, () -> authService.authUser("alice", "validHash"));
    }


}