package com.selfvault.desktop.ui.screens.home.secret

data class SecretListState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var listSecrets: List<String> = emptyList(),
)