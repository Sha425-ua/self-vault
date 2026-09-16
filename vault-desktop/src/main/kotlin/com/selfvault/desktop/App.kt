package com.selfvault.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.selfvault.desktop.ui.screens.home.HomeScreen
import com.selfvault.desktop.ui.screens.login.LoginScreen
import com.selfvault.desktop.ui.screens.login.LoginViewModel
import com.selfvault.desktop.ui.screens.register.RegisterScreen
import com.selfvault.desktop.ui.screens.register.RegisterViewModel
import com.selfvault.desktop.ui.screens.register.success.RegisterSuccessScreen
import com.selfvault.desktop.ui.theme.AppTypography
import com.selfvault.desktop.ui.theme.getAppColorScheme

enum class AppScreen {
    LOGIN,
    HOME,
    REGISTER,
    REGISTER_SUCCESS,
}

@Composable
fun App(appContainer: AppContainer) {
    var currencyScreen by remember { mutableStateOf(AppScreen.LOGIN) }
    var sharedUsername by remember { mutableStateOf("") }
    var sharedServerUrl by remember { mutableStateOf("http://localhost:8085") }

    MaterialTheme(
        colorScheme = getAppColorScheme() as ColorScheme,
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
                            authenticateService = appContainer.authService
                        )
                    }
                    LoginScreen(
                        viewModel = viewModel,
                        initialUsername = sharedUsername,
                        initialServerUrl = sharedServerUrl,

                        onLoginSuccess = { currencyScreen = AppScreen.HOME },
                        onNavigateToRegister = { typedUsername, typedServerUrl ->
                            sharedUsername = typedUsername
                            sharedServerUrl = typedServerUrl
                            currencyScreen = AppScreen.REGISTER
                        }
                    )
                }
                AppScreen.REGISTER -> {
                    val viewModel = remember {
                        RegisterViewModel(
                            apiClient = appContainer.apiClient,
                            registerService = appContainer.registerService
                        )
                    }
                    RegisterScreen(
                        viewModel = viewModel,
                        initialUsername = sharedUsername,
                        initialServerUrl = sharedServerUrl,

                        onRegisterSuccess = { currencyScreen = AppScreen.REGISTER_SUCCESS },
                        onLoginClicked = { typedUsername, typedServerUrl ->
                            sharedUsername = typedUsername
                            sharedServerUrl = typedServerUrl
                            currencyScreen = AppScreen.LOGIN
                        }
                    )
                }
                AppScreen.REGISTER_SUCCESS -> {
                    RegisterSuccessScreen(
                        onLoginClicked = { currencyScreen = AppScreen.LOGIN }
                    )
                }
                AppScreen.HOME -> {
                    HomeScreen()
                }
            }
        }
    }
}