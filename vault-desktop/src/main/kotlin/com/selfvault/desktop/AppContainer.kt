package com.selfvault.desktop

import com.selfvault.client.VaultApiClient
import com.selfvault.client.service.AuthenticateService
import com.selfvault.client.service.SecretService

class AppContainer {
    val apiClient = VaultApiClient("http://localhost:8085")
    val authService = AuthenticateService(apiClient)
    val secretService = SecretService(apiClient, authService)
}