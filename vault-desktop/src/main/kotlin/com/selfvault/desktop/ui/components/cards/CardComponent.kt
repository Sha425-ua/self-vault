package com.selfvault.desktop.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.selfvault.desktop.ui.theme.AppSpacing
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

@Composable
fun groupCardComponent(
    cardHeader: String?,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    dto: List<CardContentDto>,
    onClick: (String) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
        Spacer(modifier = Modifier.height(24.dp))

        if (!cardHeader.isNullOrBlank()) {
            Row {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = cardHeader,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            var id = 1
            for ((title, description, icon) in dto) {

                val outerRadius = AppSpacing.ItemCard.outerRadius
                val innerRadius = AppSpacing.ItemCard.innerRadius

                val shape = getConnectedShape(id, dto.size, outerRadius, innerRadius)
                cardComponent(
                    cornerShape = shape,
                    title = title,
                    description = description,
                    icon = icon,
                    onClick = onClick,
                )

                if (id < dto.size) {
                    Spacer(modifier = Modifier.height(AppSpacing.ItemCard.itemGap))
                }
                id++
            }
        }
    }
}

@Composable
fun cardComponent(
    cornerShape: RoundedCornerShape = RoundedCornerShape(
        topStart = AppSpacing.ItemCard.outerRadius,
        topEnd = AppSpacing.ItemCard.outerRadius,
        bottomEnd = AppSpacing.ItemCard.outerRadius,
        bottomStart = AppSpacing.ItemCard.outerRadius
    ),
    title: String,
    description: String?,
    icon: ImageVector?,
    onClick: (String) -> Unit
) {
    Card(
        shape = cornerShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        onClick = { onClick(title) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = AppSpacing.ItemCard.horizontalPadding, vertical = AppSpacing.ItemCard.verticalPadding),
            verticalAlignment = Alignment.CenterVertically) {

            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(AppSpacing.iconSize)
                )
            }
            Column {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                if (description != null) {
                    Text(
                        text = description,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
    }
}

fun getConnectedShape(id: Int, size: Int, outerRadius: Dp, innerRadius: Dp): RoundedCornerShape {
    if (size == 1) {
        return RoundedCornerShape(outerRadius)
    }

    when (id) {
        1 -> {
            return RoundedCornerShape(
                topStart = outerRadius,
                topEnd = outerRadius,
                bottomEnd = innerRadius,
                bottomStart = innerRadius,
            )
        }

        size -> {
            return RoundedCornerShape(
                topStart = innerRadius,
                topEnd = innerRadius,
                bottomEnd = outerRadius,
                bottomStart = outerRadius,
            )
        }

        else -> {
            return RoundedCornerShape(innerRadius)
        }
    }
}