package com.selfvault.desktop.ui.screens.home.secret

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.selfvault.client.service.SecretService
import com.selfvault.desktop.config.VaultSessionManager

class SecretListViewModel(
    private val secretService: SecretService,
    private val sessionManager: VaultSessionManager
) {
    var state by mutableStateOf(SecretListState())
        private set

    fun getSecretList(
        onSuccess: () -> Unit = {}
    ) {
        state = state.copy(isLoading = true)

        val username = sessionManager.getUsername()
        val masterKey = sessionManager.getMasterKeyCopy()

        try {
            var secretTitleList = secretService.listSecrets(username, masterKey)
            state = state.copy(
                isLoading = false,
                errorMessage = null,
                listSecrets = secretTitleList
            )

            onSuccess()
        } catch (e: Exception) {
            state = state.copy(
                isLoading = false,
                errorMessage = e.message ?: "Failed to load secrets"
            )
        } finally {
        }
    }
}