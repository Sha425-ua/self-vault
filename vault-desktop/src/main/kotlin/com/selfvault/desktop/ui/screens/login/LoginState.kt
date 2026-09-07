package com.selfvault.desktop.ui.screens.login

data class LoginState(
    val username: String = "",
    val serverUrl: String = "",
    val password: Char? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)