package com.selfvault.desktop

import com.selfvault.client.VaultApiClient
import com.selfvault.client.service.AuthenticateService
import com.selfvault.client.service.RegisterService
import com.selfvault.desktop.config.ConfigLoader
import com.selfvault.desktop.config.VaultSessionManager
import kotlinx.coroutines.CoroutineScope

class AppContainer(
    applicationScope: CoroutineScope
) {
    val apiClient = VaultApiClient("http://localhost:8085")
    val authService = AuthenticateService(apiClient)
    val registerService = RegisterService(apiClient)

    val configLoader = ConfigLoader()

    val sessionManager = VaultSessionManager(
        configLoader = configLoader,
        scope = applicationScope
    )
}