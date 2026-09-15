package com.selfvault.server.controller;

import com.selfvault.domain.model.SaltResponceDto;
import com.selfvault.server.service.AuthService;
import com.selfvault.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class AuthenticateController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/salt")
    public ResponseEntity<SaltResponceDto> getSalt(@RequestHeader("X-Username") String username) {
        String saltBase64 = userService.getUserSalt(username);
        log.info("Request for salt for user {} received. Salt: {}", username, saltBase64);
        return ResponseEntity.ok(new SaltResponceDto(saltBase64));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Auth-Hash") String authHash) {
        log.info("Received login request for user {}", username);
        authService.authUser(username, authHash);
        log.info("Login successfully for user {}", username);
        return ResponseEntity.ok("Authenticated successfully");
    }
}
