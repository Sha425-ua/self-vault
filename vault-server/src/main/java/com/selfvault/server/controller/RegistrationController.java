package com.selfvault.server.controller;

import com.selfvault.domain.model.RegisterRequestDto;
import com.selfvault.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto dto) {
        log.info("""
                Received new request to registration.
                Username: {}
                AuthHash: {}
                Salt: {}
                """, dto.username(), dto.authHash(), dto.salt());

        service.registerUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User " + dto.username() + " successfully registered!");
    }
}
