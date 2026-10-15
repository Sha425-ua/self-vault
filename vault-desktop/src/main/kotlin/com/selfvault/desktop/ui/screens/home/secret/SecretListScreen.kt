package com.selfvault.desktop.ui.screens.home.secret

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.selfvault.desktop.ui.components.cards.CardContentDto
import com.selfvault.desktop.ui.components.cards.groupCardComponent

@Composable
fun SecretListScreen(
    modifier: Modifier = Modifier,
    viewModel: SecretListViewModel,
    onSecretClick: (String) -> Unit = {},
) {
    val state = viewModel.state
    var selectedSecretTitle by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {viewModel.getSecretList()}

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Secrets",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                state.errorMessage != null -> {
                    Text(
                        text = state.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                state.listSecrets.isEmpty() -> {
                    Text(
                        text = "Vault is empty",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                state.listSecrets.isNotEmpty() -> {
                    val cardDto = ArrayList<CardContentDto>()

                    for (item in state.listSecrets) {
                        cardDto.add(CardContentDto(icon = Icons.Default.Lock, title = item, description = "******"))
                    }

                    groupCardComponent(
                        null,
                        dto = cardDto,
                        onClick = { selectedSecretTitle = it },
                    )
                }
            }
        }
    }

    selectedSecretTitle?.let { title ->
        Dialog(onDismissRequest = { selectedSecretTitle = null }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(400.dp).padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = selectedSecretTitle!!, style = MaterialTheme.typography.titleLarge)

                    Text("Login: user@mail.com")
                    Text("Password: ••••••••")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = { selectedSecretTitle = null }) {
                            Text("Закрыть")
                        }
                    }
                }
            }
        }
    }
}