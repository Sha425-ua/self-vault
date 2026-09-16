package com.selfvault.desktop

import com.selfvault.client.VaultApiClient
import com.selfvault.client.service.AuthenticateService
import com.selfvault.client.service.RegisterService

class AppContainer {
    val apiClient = VaultApiClient("http://localhost:8085")
    val authService = AuthenticateService(apiClient)
    val registerService = RegisterService(apiClient)
}