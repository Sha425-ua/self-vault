package com.selfvault.desktop.ui.screens.home.addsecret

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.selfvault.client.service.SecretService
import com.selfvault.crypto.AuthHashService
import com.selfvault.crypto.AuthHashService.generateAuthHash
import com.selfvault.crypto.KeyDerivationService
import com.selfvault.desktop.config.VaultSessionManager
import com.selfvault.domain.model.SecretDto
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AddNewSecretViewModel(
    private val secretService: SecretService,
    private val sessionService: VaultSessionManager
) {

    var state by mutableStateOf(AddNewSecretState())
        private set

    val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineName("AddNewSecretCoroutine")
    )

    fun addNewSecret(
        title: String,
        login: String,
        secret: CharArray,
        note: String,
        onSuccess: () -> Unit = {}
    ) {
        state.isLoading = true

        viewModelCoroutineScope.launch {
            var masterKey: ByteArray?
            val activeSelfVaultUsername: String?

            try {
                masterKey = sessionService.getMasterKeyCopy()
                activeSelfVaultUsername = sessionService.getUsername()

                val dto = SecretDto.builder(title, secret)
                    .addNote(note)
                    .addLogin(login)
                    .build()

                secretService.addNewSecret(activeSelfVaultUsername, masterKey, dto)

                onSuccess()
            } catch (ex: Exception) {
                state = state.copy(
                    isLoading = false,
                    errorMessage = ex.message,
                )
            } finally {
                KeyDerivationService.wipe(secret)
            }
        }
    }
}