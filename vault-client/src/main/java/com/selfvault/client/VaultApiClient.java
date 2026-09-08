package com.selfvault.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfvault.domain.exception.AuthException;
import com.selfvault.domain.exception.SecretNotFoundException;
import com.selfvault.domain.exception.ServerException;
import com.selfvault.domain.exception.UserNotFoundException;
import com.selfvault.domain.model.RegisterRequestDto;
import com.selfvault.domain.model.SaltResponceDto;
import com.selfvault.domain.model.SecretRequestDto;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class VaultApiClient {
    private final HttpClient httpClient;
    private volatile String serverUrl;
    private final ObjectMapper objectMapper;

    public VaultApiClient(String serverUrl) {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.serverUrl = serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getServerUrl() {
        return serverUrl;
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
                case 200, 201 -> {}
                case 500 -> throw new ServerException("Internal server error");
                case 400, 409 -> throw new ServerException("Bad request: " + response.body());
                default -> throw new ServerException("Unexpected server response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Network connection failed: " + e.getMessage());
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
            throw new ServerException("Network connection failed: " + e.getMessage());
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
                case 200, 201 -> {}
                case 401 -> throw new AuthException("Invalid password");
                case 404 -> throw new UserNotFoundException("User '" + username + "' not found on server.");
                case 400, 409 -> throw new ServerException("Bad request: " + response.body());
                case 500 -> throw new ServerException("Internal server error");
                default -> throw new ServerException("Unexpected server response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Network connection failed: " + e.getMessage());
        }
    }

    public void deleteSecret(String username, String authHash, String title) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/api/secret/delete?title=" + encodedTitle))
                    .header("X-Username", username)
                    .header("X-Auth-Hash", authHash)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200, 204 -> {}
                case 401 -> throw new AuthException("Invalid password");
                case 404 -> throw new UserNotFoundException("User '" + username + "' not found on server.");
                case 400 -> throw new ServerException("Bad request: " + response.body());
                case 500 -> throw new ServerException("Internal server error");
                default -> throw new ServerException("Unexpected server response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Network connection failed: " + e.getMessage());
        }
    }

    public List<String> listSecrets(String username, String authHash) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/api/secret/list"))
                    .header("X-Username", username)
                    .header("X-Auth-Hash", authHash)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200 -> {
                    return objectMapper.readValue(
                            response.body(),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
                    );
                }
                case 401 -> throw new AuthException("Invalid password");
                case 404 -> throw new UserNotFoundException("User '" + username + "' not found on server.");
                case 500 -> throw new ServerException("Internal server error");
                default -> throw new ServerException("Unexpected server response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Network connection failed: " + e.getMessage());
        }
    }

    public String getEncryptedSecret(String username, String title, String authHash) {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/api/secret/get?title=" + encodedTitle))
                    .headers("X-Username", username, "X-Auth-Hash", authHash)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200 -> { return response.body(); }
                case 401 -> throw new AuthException("Invalid password");
                case 404 -> throw new SecretNotFoundException("Secret not found for user " + username + ".");
                case 500 -> throw new ServerException("Internal server error");
                default -> throw new ServerException("Unexpected server response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Network connection failed: " + e.getMessage());
        }
    }

    public void login(String username, String authHash) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/api/auth/login"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .headers("X-Username", username, "X-Auth-Hash", authHash)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200, 201 -> {}
                case 401 -> throw new AuthException("Invalid password");
                case 404 -> throw new UserNotFoundException("User '" + username + "' not found on server.");
                case 500 -> throw new ServerException("Internal server error");
                default -> throw new ServerException("Unexpected server response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Network connection failed: " + e.getMessage());
        }
    }
}
