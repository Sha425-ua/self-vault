package com.selfvault.server.service;

import com.selfvault.domain.model.RegisterRequestDto;
import com.selfvault.server.entity.UserEntity;
import com.selfvault.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.selfvault.domain.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public void registerUser(RegisterRequestDto dto) {
        if (repository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("User already exist with this username!");
        }

        UserEntity entity = UserEntity.fromDto(dto);
        repository.save(entity);
    }

    public String getUserSalt(String username) {
        return repository.findSaltByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
