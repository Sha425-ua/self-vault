package com.selfvault.desktop.ui.theme.providers

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import java.io.File

data class CaelestiaScheme(
    val mode: String,
    val colours: Map<String, String>
)

val mapper = jacksonMapperBuilder()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .build()!!

fun loadCaelestiaColorSchemeIfAvailable(): ColorScheme? {
    val file = File(System.getProperty("user.home"), ".local/state/caelestia/scheme.json")
    if (!file.exists()) return null

    return try {
        val scheme = mapper.readValue(file, CaelestiaScheme::class.java)
        createColorSchemeFromCaelestia(scheme)
    } catch (_: Exception) {
        null
    }
}

fun createColorSchemeFromCaelestia(scheme: CaelestiaScheme): ColorScheme {
    val isDark = scheme.mode == "dark"
    val c = scheme.colours

    val base = if (isDark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = c["primary"]?.let { hexToColor(it) } ?: base.onPrimary,
        onPrimary = c["onPrimary"]?.let { hexToColor(it) } ?: base.onPrimary,
        primaryContainer = c["primaryContainer"]?.let { hexToColor(it) } ?: base.primaryContainer,
        onPrimaryContainer = c["onPrimaryContainer"]?.let { hexToColor(it) } ?: base.onPrimaryContainer,
        secondary = c["secondary"]?.let { hexToColor(it) } ?: base.secondary,
        onSecondary = c["onSecondary"]?.let { hexToColor(it) } ?: base.onSecondary,
        secondaryContainer = c["secondaryContainer"]?.let { hexToColor(it) } ?: base.secondaryContainer,
        onSecondaryContainer = c["onSecondaryContainer"]?.let { hexToColor(it) } ?: base.onSecondaryContainer,
        tertiary = c["tertiary"]?.let { hexToColor(it) } ?: base.tertiary,
        onTertiary = c["onTertiary"]?.let { hexToColor(it) } ?: base.onTertiary,
        background = c["background"]?.let { hexToColor(it) } ?: base.background,
        onBackground = c["onBackground"]?.let { hexToColor(it) } ?: base.onBackground,
        surface = c["surface"]?.let { hexToColor(it) } ?: base.surface,
        onSurface = c["onSurface"]?.let { hexToColor(it) } ?: base.onSurface,
        surfaceVariant = c["surfaceVariant"]?.let { hexToColor(it) } ?: base.surfaceVariant,
        onSurfaceVariant = c["onSurfaceVariant"]?.let { hexToColor(it) } ?: base.onSurfaceVariant,
        outline = c["outline"]?.let { hexToColor(it) } ?: base.outline,
        outlineVariant = c["outlineVariant"]?.let { hexToColor(it) } ?: base.outlineVariant,
        error = c["error"]?.let { hexToColor(it) } ?: base.error
    )
}

fun hexToColor(hex: String): Color {
    val clean = hex.removePrefix("#")
    val colorLong = "FF$clean".toLong(16)
    return Color(colorLong)
}
