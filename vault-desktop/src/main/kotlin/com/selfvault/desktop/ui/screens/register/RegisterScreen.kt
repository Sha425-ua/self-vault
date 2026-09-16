package com.selfvault.desktop.ui.screens.register

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.selfvault.desktop.ui.theme.AppSpacing

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    initialUsername: String = "",
    initialServerUrl: String = "http://localhost:8085",
    onLoginClicked: (String, String) -> Unit = {_, _ ->},
    onRegisterSuccess: () -> Unit = {}
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var serverUrl by remember { mutableStateOf(initialServerUrl) }
        var username by remember { mutableStateOf(initialUsername) }
        val passwordState = rememberTextFieldState()
        var isPasswordVisibility by remember { mutableStateOf(false) }

        val state = viewModel.state

        Card(
            modifier = Modifier
                .width(AppSpacing.cardWidth)
                .height(AppSpacing.cardHeight)
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
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppSpacing.cardPadding)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(AppSpacing.iconSize)
                )

                Spacer(modifier = Modifier.height(AppSpacing.iconSpacing))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "SelfVault",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.titleSubtitleSpacing))
                    Text(
                        text = "Register your account",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.sectionSpacing))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.fieldSpacing)
                ) {
                    OutlinedTextField(
                        value = serverUrl,
                        onValueChange = { serverUrl = it },
                        label = { Text("Server URL") },
                        singleLine = true,
                        shape = RoundedCornerShape(AppSpacing.fieldCornerRadius),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = { Text("Username") },
                        singleLine = true,
                        shape = RoundedCornerShape(AppSpacing.fieldCornerRadius),
                        modifier = Modifier.fillMaxWidth()
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
                            state = passwordState,
                            textObfuscationMode = if (isPasswordVisibility)
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
                                if (passwordState.text.isEmpty()) {
                                    Text(
                                        text = "Master Password",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                innerTextField()
                            }
                        )
                        IconButton(
                            onClick = { isPasswordVisibility = !isPasswordVisibility },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = if (isPasswordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle password"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.sectionSpacing))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.buttonSpacing),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.errorMessage != null) {
                        Text(
                            text = state.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Button(
                        onClick = {
                            val buffer = passwordState.text

                            val passwordChars = CharArray(buffer.length) { index -> buffer[index] }
                            passwordState.clearText()

                            viewModel.onRegisterClicked(username, passwordChars, serverUrl, onSuccess = onRegisterSuccess)
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
                                text = "Register",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    TextButton(
                        onClick = { onLoginClicked(username, serverUrl) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Already have account?")
                    }
                }
            }
        }
    }
}