package com.selfvault.desktop.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.selfvault.client.VaultApiClient
import com.selfvault.client.service.AuthenticateService
import com.selfvault.client.service.RegisterService
import com.selfvault.crypto.KeyDerivationService
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LoginViewModel(
    private val apiClient: VaultApiClient,
    private val authenticateService: AuthenticateService
) {
    var state by mutableStateOf(LoginState())
        private set

    val registerService = RegisterService(apiClient)

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineName("LoginRequestCoroutine")
    )
    fun onLoginClicked(username: String, password: CharArray, serverUrl: String, onSuccess: () -> Unit = {}) {
        if (username.isBlank() || password.isEmpty() || serverUrl.isBlank()) {
            state = state.copy(
                isLoading = false,
                errorMessage = "Please, fill in all fields"
            )
            KeyDerivationService.wipe(password)
            return
        }

        state = state.copy(isLoading = true, errorMessage = null)

        viewModelCoroutineScope.launch {
            try {
                apiClient.serverUrl = serverUrl.trimEnd('/')

                authenticateService.login(username, password)
                KeyDerivationService.wipe(password)

                state = state.copy(isLoading = false, errorMessage = null)
                onSuccess()
            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Authentication failed"
                )
            } finally {
                KeyDerivationService.wipe(password)
            }
        }
    }

    fun onRegisterClicked() {

    }
}