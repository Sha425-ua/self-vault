package com.selfvault.desktop.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.selfvault.desktop.ui.theme.providers.loadCaelestiaColorSchemeIfAvailable

@Composable
fun getAppColorScheme(): Any {
    val isDark = isSystemInDarkTheme()

    val caelestiaScheme = loadCaelestiaColorSchemeIfAvailable()

    if (caelestiaScheme != null) return caelestiaScheme

    return if (isDark) darkColorScheme() else lightColorScheme()
}