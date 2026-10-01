package com.selfvault.desktop.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.selfvault.crypto.KeyDerivationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes


class VaultSessionManager(
    private val configLoader: ConfigLoader,
    val scope: CoroutineScope
) {

    var isUnlocked by mutableStateOf(false)
        private set

    private var timerJob: Job? = null

    var masterKey: ByteArray? = null

    fun unlock(key: ByteArray) {
        this.masterKey = key
        this.isUnlocked = true
        resetTimer()
    }

    fun resetTimer() {
        timerJob?.cancel()

        val minutes = configLoader.config.appTimerTimeoutDuration
        if (minutes > 0) {
            timerJob = scope.launch {
                delay(minutes.minutes)
                lock()
            }
        }
    }

    fun lock() {
        timerJob?.cancel()
        timerJob = null

        masterKey?.let { KeyDerivationService.wipe(it) }
        masterKey = null

        isUnlocked = false
    }

    fun getMasterKeyCopy(): ByteArray {
        val key = masterKey ?: throw IllegalStateException("Vault is locked")
        resetTimer()

        return key.copyOf()
    }
}