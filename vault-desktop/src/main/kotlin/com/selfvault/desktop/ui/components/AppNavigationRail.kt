package com.selfvault.desktop.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.selfvault.desktop.ui.theme.AppSpacing

enum class NavigationSection(
    val label: String,
    val icon: ImageVector,
) {
    ADD_NEW_SECRET(label = "Create new secret", icon = Icons.Default.Edit),
    SECRET_LIST(label = "All item", icon = Icons.Default.Home),
    SETTINGS(label = "Settings", icon = Icons.Default.Settings),
}

@Composable
fun AppNavigationRail(
    selectedSection: NavigationSection,
    onSectionSelected: (NavigationSection) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val sidebarWidth by animateDpAsState(
        targetValue = if (isExpanded) 240.dp else 80.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "sidebarWidth"
    )

    Surface(
        modifier = modifier
            .width(sidebarWidth)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.85f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isExpanded) Arrangement.SpaceBetween else Arrangement.Center
            ) {
                SidebarHeader(isExpanded, onToggle = {isExpanded = !isExpanded})
            }

            AddNewSecretButton(
                isExpanded,
                onClick = { onSectionSelected(NavigationSection.ADD_NEW_SECRET)},
                section = NavigationSection.ADD_NEW_SECRET,
                modifier = Modifier.height(56.dp).padding(horizontal = 12.dp)
            )

            Spacer(Modifier.height(8.dp))

            NavigationSection.entries
                .filter { it != NavigationSection.ADD_NEW_SECRET }
                .forEach { section ->
                NavigationItem(
                    icon = section.icon,
                    label = section.label,
                    isSelected = (section == selectedSection),
                    isExpanded = isExpanded,
                    onClick = { onSectionSelected(section) },
                    modifier = Modifier.height(56.dp).padding(horizontal = 12.dp)
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun SidebarHeader(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isExpanded) Arrangement.SpaceBetween else Arrangement.Center
    ) {
        if (isExpanded) {
            Text(
                text = "self-vault",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                maxLines = 1,
                softWrap = false
            )
        }
        IconButton(onClick = onToggle) {
            Icon(
                imageVector = if (isExpanded) Icons.AutoMirrored.Filled.MenuOpen else Icons.Default.Menu,
                contentDescription = "Toggle Sidebar",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AddNewSecretButton(
    isExpanded: Boolean,
    onClick: () -> Unit = {},
    section: NavigationSection,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.tertiary,
        onClick = onClick,
        contentColor = MaterialTheme.colorScheme.onTertiary,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = section.icon,
                    contentDescription = "Create new secret",
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = section.label,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

@Composable
fun NavigationItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit = {},
    isExpanded: Boolean,
    modifier: Modifier
) {
    Surface(
        selected = isSelected,
        onClick = onClick,
        shape = RoundedCornerShape(AppSpacing.cardCornerRadius),
        color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
        contentColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(24.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Text(
                    text = label,

                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    }
}