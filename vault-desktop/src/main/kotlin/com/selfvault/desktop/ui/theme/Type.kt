package com.selfvault.desktop.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontFamily

@OptIn(ExperimentalTextApi::class)
val RubikFontFamily = FontFamily("Rubik")
private val default = Typography()

val AppTypography = Typography(
    headlineLarge  = default.headlineLarge.copy(fontFamily = RubikFontFamily),
    headlineMedium = default.headlineMedium.copy(fontFamily = RubikFontFamily),
    titleLarge     = default.titleLarge.copy(fontFamily = RubikFontFamily),
    titleMedium    = default.titleMedium.copy(fontFamily = RubikFontFamily),
    bodyLarge      = default.bodyLarge.copy(fontFamily = RubikFontFamily),
    bodyMedium     = default.bodyMedium.copy(fontFamily = RubikFontFamily),
    labelLarge     = default.labelLarge.copy(fontFamily = RubikFontFamily),
    labelMedium    = default.labelMedium.copy(fontFamily = RubikFontFamily),
)
