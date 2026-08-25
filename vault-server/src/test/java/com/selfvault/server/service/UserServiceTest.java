package com.selfvault.server.service;

import com.selfvault.domain.exception.UserNotFoundException;
import com.selfvault.domain.model.RegisterRequestDto;
import com.selfvault.server.entity.UserEntity;
import com.selfvault.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Test
    void registerUser_ShouldRegisterNewUserIfHeDoesNotExistsAlready() {
        when(repository.existsByUsername("alice")).thenReturn(false);
        RegisterRequestDto dto = new RegisterRequestDto("alice", null, null);

        service.registerUser(dto);

        verify(repository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void registerUser_WhenUserAlreadyRegistered_ShouldThrowException() {
        when(repository.existsByUsername("alice")).thenReturn(true);
        RegisterRequestDto dto = new RegisterRequestDto("alice", null, null);

        assertThrows(IllegalArgumentException.class, () -> service.registerUser(dto));

        verify(repository, never()).save(any());
    }

    @Test
    void getUserSalt_WhenUserNotFound_ShouldThrowException() {
        when(repository.findSaltByUsername("alice")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getUserSalt("alice"));
    }

    @Test
    void getUserSalt_WhenUserExists_ShouldReturnSalt() {
        when(repository.findSaltByUsername("alice")).thenReturn(Optional.of("base64Salt"));

        String salt = service.getUserSalt("alice");

        assertEquals("base64Salt", salt);
    }
}