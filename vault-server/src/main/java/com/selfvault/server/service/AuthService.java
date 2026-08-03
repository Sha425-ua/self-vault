package com.selfvault.server.service;

import com.selfvault.domain.exception.AuthException;
import com.selfvault.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;

    public void authUser(String username, String authHash) {
        boolean isAuthenticated = repository.existsByUsernameAndAuthHash(username, authHash);
        if (!isAuthenticated) {
            throw new AuthException("User or password wrong");
        }
    }
}
