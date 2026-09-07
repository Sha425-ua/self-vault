package com.selfvault.desktop

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.selfvault.desktop.ui.screens.login.LoginScreen
import com.selfvault.desktop.ui.theme.AppTypography

enum class AppScreen {
    LOGIN
}

@Composable
fun App() {
    var currencyScreen by remember { mutableStateOf(AppScreen.LOGIN) }

    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = AppTypography
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            when (currencyScreen) {
                AppScreen.LOGIN -> {
                    LoginScreen()
                }
            }
        }
    }
}