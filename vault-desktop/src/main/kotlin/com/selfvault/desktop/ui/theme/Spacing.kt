package com.selfvault.desktop.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AppSpacing {
    object DialogCard {
        val width: Dp = 400.dp
        val height: Dp = 550.dp
        val padding: Dp = 32.dp
        val cornerRadius: Dp = 32.dp
        val elevation: Dp = 8.dp
    }

    object ItemCard {
        val outerRadius: Dp = 24.dp
        val innerRadius: Dp = 4.dp
        val itemGap: Dp = 2.dp
        val horizontalPadding: Dp = 16.dp
        val verticalPadding: Dp = 12.dp
    }

    val sectionSpacing: Dp = 24.dp

    val fieldSpacing: Dp = 12.dp
    val fieldHeight: Dp = 56.dp
    val fieldCornerRadius: Dp = 32.dp

    val buttonSpacing: Dp = 8.dp
    val buttonHeight: Dp = 48.dp
    val buttonCornerRadius: Dp = 32.dp

    val iconSize: Dp = 36.dp
    val iconSpacing: Dp = 16.dp
    val titleSubtitleSpacing: Dp = 6.dp
}
