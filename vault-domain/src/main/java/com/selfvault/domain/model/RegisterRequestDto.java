package com.selfvault.domain.model;

public record RegisterRequestDto(
        String username,
        String authHash,
        String salt
) {
}
