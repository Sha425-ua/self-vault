package com.selfvault.desktop.ui.components.cards

import androidx.compose.ui.graphics.vector.ImageVector

data class CardContentDto(
    val title: String,
    val description: String? = null,
    val icon: ImageVector? = null,
    val trailingContent: String? = null,
)