package com.selfvault.desktop

import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

fun main() {
    System.setProperty("skiko.linux.autodetect", "true")
    System.setProperty("sun.java2d.opengl", "true")

    val isDebug = System.getProperty("vault.debug")?.toBoolean() ?: false

    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob() + CoroutineName("Application"))
    val objectMapper = ObjectMapper().registerKotlinModule()

    val appContainer = AppContainer(
        applicationScope,
        objectMapper
    )

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Self-Vault",
        ) {
            LaunchedEffect(window) {
                window.addComponentListener(object : ComponentAdapter() {
                    override fun componentResized(e: ComponentEvent?) {
                        window.revalidate()
                    }
                })
            }
            App(
                appContainer = appContainer,
                debug = isDebug
            )
        }
    }
}