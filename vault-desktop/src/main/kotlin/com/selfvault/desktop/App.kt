package com.selfvault.desktop

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.selfvault.client.service.RegisterService
import com.selfvault.desktop.ui.screens.login.LoginScreen
import com.selfvault.desktop.ui.screens.login.LoginViewModel
import com.selfvault.desktop.ui.theme.AppTypography

enum class AppScreen {
    LOGIN,
    HOME
}

@Composable
fun App(appContainer: AppContainer) {
    var currencyScreen by remember { mutableStateOf(AppScreen.LOGIN) }

    MaterialTheme(
        colorScheme = darkColorScheme(),
        typography = AppTypography
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            when (currencyScreen) {
                AppScreen.LOGIN -> {
                    val viewModel = remember {
                        LoginViewModel(
                            apiClient = appContainer.apiClient,
                            authenticateService = appContainer.authService,
                        )
                    }
                    LoginScreen(
                        viewModel = viewModel,
                        onLoginSuccess = {
                            currencyScreen = AppScreen.HOME
                        }
                    )
                }
                AppScreen.HOME -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            text = "Добро пожаловать в Главное меню!",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}