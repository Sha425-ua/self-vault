package com.selfvault.server.exeption;

import com.selfvault.domain.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.selfvault.domain.exception.UserNotFoundException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    @SuppressWarnings("unused")
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthException.class)
    @SuppressWarnings("unused")
    public ResponseEntity<String> handleAuthException(AuthException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }
}
