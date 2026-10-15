package com.selfvault.desktop.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.selfvault.desktop.ui.components.AppNavigationRail
import com.selfvault.desktop.ui.components.NavigationSection
import androidx.compose.runtime.setValue
import com.selfvault.desktop.AppContainer
import com.selfvault.desktop.ui.screens.home.addsecret.AddNewSecretScreen
import com.selfvault.desktop.ui.screens.home.addsecret.AddNewSecretViewModel
import com.selfvault.desktop.ui.screens.home.secret.SecretListScreen
import com.selfvault.desktop.ui.screens.home.secret.SecretListViewModel
import com.selfvault.desktop.ui.screens.home.settings.SettingsScreen
import java.util.logging.Logger


private val logger = Logger.getLogger("HomeScreen")
@Composable
fun HomeScreen(
    viewModel: AddNewSecretViewModel,
    appContainer: AppContainer
) {
    var currentSection by remember { mutableStateOf(NavigationSection.SETTINGS) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AppNavigationRail(
                selectedSection = currentSection,
                onSectionSelected = { newSection ->
                    currentSection = newSection
                }
            )
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 0.dp, top = 12.dp, end = 12.dp, bottom = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val startPadding = maxWidth * 0.20f
                    val endPadding = maxWidth * 0.15f

                    Box(modifier = Modifier.padding(start = startPadding, end = endPadding, top = 16.dp, bottom = 16.dp)) {
                        when (currentSection) {
                            NavigationSection.ADD_NEW_SECRET -> {
                                AddNewSecretScreen(
                                    viewModel = viewModel,
                                    onClick = { currentSection = NavigationSection.SECRET_LIST }
                                )
                                logger.info("Navigation section changed to: $currentSection")
                            }

                            NavigationSection.SECRET_LIST -> {
                                val viewModel = remember {
                                    SecretListViewModel(
                                        secretService = appContainer.secretService,
                                        sessionManager = appContainer.sessionManager
                                    )
                                }
                                SecretListScreen(
                                    viewModel = viewModel
                                )
                            }

                            NavigationSection.SETTINGS -> {
                                SettingsScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}