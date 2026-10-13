package com.selfvault.desktop.ui.screens.home.secret

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector

data class SecretCardContentDto(
    var icon : ImageVector? = null,
    var title : String,
    var login: String,
) {}
