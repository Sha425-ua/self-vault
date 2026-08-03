package com.selfvault.server.controller;

import com.selfvault.domain.model.SecretRequestDto;
import com.selfvault.server.service.AuthService;
import com.selfvault.server.service.SecretService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secret")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class SecretController {
    private final AuthService authService;
    private final SecretService secretService;

    @PostMapping("/add")
    public ResponseEntity<String> addNewSecret(
            @RequestHeader("X-Auth-Hash") String authHash,
            @RequestHeader("X-Username") String username,
            @RequestBody SecretRequestDto dto) {
        log.info("Received new request to save secret for user {} with title {}",
                username, dto.title());

        authService.authUser(username, authHash);


        secretService.saveSecret(username, dto);

        log.info("New secret with title {} for user {} saved successfully",
                dto.title(), username);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Secret from " + username + " saved successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteSecret(
            @RequestHeader("X-Auth-Hash") String authHash,
            @RequestHeader("X-Username") String username,
            @RequestParam("title") String title) {
        log.info("Received new request to delete secret for user {} with title {}",
                username, title);

        secretService.deleteSecret(username, title);

        log.info("Secret with title {} for user {} deleted successfully",
                title, username);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
