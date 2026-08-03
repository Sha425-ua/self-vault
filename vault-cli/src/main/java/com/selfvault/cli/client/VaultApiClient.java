package com.selfvault.cli.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfvault.domain.exception.AuthException;
import com.selfvault.domain.exception.ServerException;
import com.selfvault.domain.exception.UserNotFoundException;
import com.selfvault.domain.model.RegisterRequestDto;
import com.selfvault.domain.model.SaltResponceDto;
import com.selfvault.domain.model.SecretRequestDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class VaultApiClient {
    private final HttpClient httpClient;
    private final String serverUrl;
    private final ObjectMapper objectMapper;

    public VaultApiClient(String serverUrl) {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.serverUrl = serverUrl;
    }

    public void register(RegisterRequestDto dto) {
        try {
            String jsonBody = objectMapper.writeValueAsString(dto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/api/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200, 201 -> System.out.println("User register successfully.");
                case 500 -> throw new ServerException("Internal server error");
                case 400, 409 -> throw new ServerException("Bad request: " + response.body());
                default -> throw new ServerException("Unexpected server response: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error while connection: " + e.getMessage());
        }
    }

    public SaltResponceDto getSalt(String username) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/api/auth/salt"))
                    .header("X-Username", username)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200 -> { return objectMapper.readValue(response.body(), SaltResponceDto.class); }
                case 404 -> throw new UserNotFoundException("User '" + username + "' not found on server.");
                case 500 -> throw new ServerException("Internal server error");
                default -> throw new ServerException("Unexpected server response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Network error while getting salt: " + e.getMessage(), e);
        }
    }

    public void sendSecret(String username, String authHash, SecretRequestDto dto) {
        try {
            String jsonBody = objectMapper.writeValueAsString(dto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/api/secret/add"))
                    .header("Content-Type", "application/json")
                    .header("X-Username", username)
                    .header("X-Auth-Hash", authHash)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 401 -> throw new AuthException("Invalid password");
                case 404 -> throw new UserNotFoundException("User '" + username + "' not found on server.");
                case 400, 409 -> throw new ServerException("Bad request: " + response.body());
                case 500 -> throw new ServerException("Internal server error");
                default -> throw new ServerException("Unexpected server response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
