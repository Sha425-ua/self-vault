package com.selfvault.domain.model;

public record SecretRequestDto(
        String title,
        String encryptedData) {
}
