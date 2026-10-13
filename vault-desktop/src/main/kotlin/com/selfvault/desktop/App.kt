package com.selfvault.desktop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.selfvault.crypto.AuthHashService
import com.selfvault.crypto.KeyDerivationService
import com.selfvault.desktop.ui.screens.home.HomeScreen
import com.selfvault.desktop.ui.screens.home.addsecret.AddNewSecretViewModel
import com.selfvault.desktop.ui.screens.login.LoginScreen
import com.selfvault.desktop.ui.screens.login.LoginViewModel
import com.selfvault.desktop.ui.screens.register.RegisterScreen
import com.selfvault.desktop.ui.screens.register.RegisterViewModel
import com.selfvault.desktop.ui.screens.register.success.RegisterSuccessScreen
import com.selfvault.desktop.ui.theme.AppTypography
import com.selfvault.desktop.ui.theme.getAppColorScheme
import io.github.oshai.kotlinlogging.KotlinLogging

enum class AppScreen {
    LOGIN,
    HOME,
    REGISTER,
    REGISTER_SUCCESS,
}

private val logger = KotlinLogging.logger {}

@Composable
fun App(appContainer: AppContainer, debug: Boolean) {
    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
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
            if (debug) {
                bypassLogin(appContainer)
            }

            LaunchedEffect(appContainer.sessionManager.isUnlocked) {
                if (!appContainer.sessionManager.isUnlocked && currentScreen == AppScreen.HOME) {
                    currentScreen = AppScreen.LOGIN
                }
            }

            when (currentScreen) {
                AppScreen.LOGIN -> {
                    val viewModel = remember {
                        LoginViewModel(
                            apiClient = appContainer.apiClient,
                            authenticateService = appContainer.authService,
                            sessionManager = appContainer.sessionManager
                        )
                    }
                    LoginScreen(
                        viewModel = viewModel,
                        initialUsername = sharedUsername,
                        initialServerUrl = sharedServerUrl,

                        onLoginSuccess = { currentScreen = AppScreen.HOME },
                        onNavigateToRegister = { typedUsername, typedServerUrl ->
                            sharedUsername = typedUsername
                            sharedServerUrl = typedServerUrl
                            currentScreen = AppScreen.REGISTER
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

                        onRegisterSuccess = { currentScreen = AppScreen.REGISTER_SUCCESS },
                        onLoginClicked = { typedUsername, typedServerUrl ->
                            sharedUsername = typedUsername
                            sharedServerUrl = typedServerUrl
                            currentScreen = AppScreen.LOGIN
                        }
                    )
                }
                AppScreen.REGISTER_SUCCESS -> {
                    RegisterSuccessScreen(
                        onLoginClicked = { currentScreen = AppScreen.LOGIN }
                    )
                }
                AppScreen.HOME -> {
                    val viewModel = remember {
                        AddNewSecretViewModel(
                            secretService = appContainer.secretService,
                            sessionService = appContainer.sessionManager,
                        )
                    }
                    HomeScreen(
                        viewModel = viewModel,
                        appContainer = appContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun bypassLogin(appContainer: AppContainer) {
    LaunchedEffect(Unit) {
        logger.info { "[DEBUG] Bypass login..." }

        val username = "test"
        val password = "test".toCharArray()

        try {
            appContainer.registerService.register(username, password.clone())
        } catch (_: Exception) {
            logger.info { "[DEBUG] User already registered." }
        }

        var masterKey: ByteArray? = null

        try {
            masterKey = appContainer.authService.loginAndDeriveKey(username, password.clone())
            appContainer.sessionManager.unlock(masterKey.clone(), username)
        } catch (e: Exception) {
            logger.error(e) { "[ERROR] Error while bypass login" }
            throw e
        } finally {
            KeyDerivationService.wipe(masterKey)
        }
        logger.info { "[DEBUG] Bypass successfully finished!" }
    }
}