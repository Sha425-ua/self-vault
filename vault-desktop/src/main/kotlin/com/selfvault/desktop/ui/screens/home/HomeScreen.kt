package com.selfvault.desktop.ui.screens.home

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
import com.selfvault.desktop.ui.theme.AppSpacing
import androidx.compose.runtime.setValue
import com.selfvault.desktop.ui.screens.home.addsecret.AddNewSecretScreen
import com.selfvault.desktop.ui.screens.home.addsecret.AddNewSecretViewModel
import com.selfvault.desktop.ui.screens.home.settings.SettingsScreen
import java.util.logging.Logger


private val logger = Logger.getLogger("HomeScreen")
@Composable
fun HomeScreen(viewModel: AddNewSecretViewModel) {
    var currentSection by remember { mutableStateOf(NavigationSection.SETTINGS) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.70f)
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
                    .weight(1f)
                    .fillMaxSize()
                    .padding(start = 0.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.70f)
                ),
                shape = RoundedCornerShape(AppSpacing.cardCornerRadius)
            ) {
                when (currentSection) {
                    NavigationSection.ADD_NEW_SECRET -> {
                        AddNewSecretScreen(
                            viewModel = viewModel,
                            onClick = { currentSection = NavigationSection.PASSWORD }
                        )
                        logger.info("Navigation section changed to: $currentSection")
                    }

                    NavigationSection.PASSWORD -> {

                    }

                    NavigationSection.SETTINGS -> {
                        SettingsScreen()
                    }
                }
            }
        }
    }
}