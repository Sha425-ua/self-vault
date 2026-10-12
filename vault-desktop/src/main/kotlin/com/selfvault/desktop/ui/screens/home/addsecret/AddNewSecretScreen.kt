package com.selfvault.desktop.ui.screens.home.addsecret


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.selfvault.desktop.ui.theme.AppSpacing

@Composable
fun AddNewSecretScreen(
    viewModel: AddNewSecretViewModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var title by remember { mutableStateOf("") }
        var login by remember { mutableStateOf("") }
        val secretState = rememberTextFieldState()
        var isSecretVisibility by remember { mutableStateOf(false) }
        var note by remember { mutableStateOf("") }

        val state = viewModel.state

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(AppSpacing.cardPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalIconButton(
                        onClick = { },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(AppSpacing.iconSpacing))
                    Text(
                        text = "Add New Secret",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(AppSpacing.titleSubtitleSpacing))
                Text(
                    text = "Specify details for your new secure item.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.sectionSpacing))

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    shape = RoundedCornerShape(AppSpacing.fieldCornerRadius),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Label,
                            contentDescription = null
                        )
                    }
                )
                Text(
                    text = "Your custom title"
                )

                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = { Text("Login") },
                    singleLine = true,
                    shape = RoundedCornerShape(AppSpacing.fieldCornerRadius),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Login,
                            contentDescription = null
                        )
                    }
                )
                Text(
                    text = "Your custom login"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppSpacing.fieldHeight)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(AppSpacing.fieldCornerRadius)
                        )
                        .padding(start = 20.dp, end = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicSecureTextField(
                        state = secretState,
                        textObfuscationMode = if (isSecretVisibility)
                            TextObfuscationMode.Visible
                        else
                            TextObfuscationMode.RevealLastTyped,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp),
                        decorator = { innerTextField ->
                            if (secretState.text.isEmpty()) {
                                Text(
                                    text = "Secret",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            innerTextField()
                        }
                    )
                    IconButton(
                        onClick = { isSecretVisibility = !isSecretVisibility },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = if (isSecretVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password"
                        )
                    }
                }

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note") },
                    singleLine = true,
                    shape = RoundedCornerShape(AppSpacing.fieldCornerRadius),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.sectionSpacing))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.buttonSpacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.errorMessage != null) {
                    Text(
                        text = state.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Button(
                    onClick = {
                        val bufferSecret = secretState.text

                        val secretChars = CharArray(bufferSecret.length) { index -> bufferSecret[index] }

                        secretState.clearText()

                        viewModel.addNewSecret(title, login, secretChars, note, onSuccess = onClick)
                    },
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(AppSpacing.buttonCornerRadius),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppSpacing.buttonHeight)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Add new secret",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

//                TextButton(
//                    onClick = { onLoginClicked(username, serverUrl) },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Already have account?")
//                }

            }
        }

/*        Card(
            modifier = Modifier
                .width(AppSpacing.cardWidth)
                .height(600.dp)
                .shadow(
                    elevation = AppSpacing.cardElevation,
                    shape = RoundedCornerShape(AppSpacing.cardCornerRadius),
                    clip = false
                ),
            shape = RoundedCornerShape(AppSpacing.cardCornerRadius),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {

        }*/
    }
}
