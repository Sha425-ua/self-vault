package com.selfvault.server.service;

import com.selfvault.domain.model.SecretRequestDto;
import com.selfvault.server.entity.SecretEntity;
import com.selfvault.server.repository.SecretRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecretService {
    private final SecretRepository repository;

    public void saveSecret(String username, SecretRequestDto dto) {
        SecretEntity entity = new SecretEntity();
        entity.setUsername(username);
        entity.setTitle(dto.title());
        entity.setEncryptedData(dto.encryptedData());

        repository.save(entity);
    }

    public void deleteSecret(String username, String title) {
        boolean isEntityExists = repository.existsByUsernameAndTitle(username, title);
        if (!isEntityExists) {
            throw new IllegalArgumentException("Secret with title " + title + " for user " + username + " does not exist.");
        }

        repository.deleteByUsernameAndTitle(username, title);
    }
}