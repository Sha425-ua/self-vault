package com.selfvault.desktop

import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

fun main() {
    System.setProperty("skiko.linux.autodetect", "true")
    System.setProperty("sun.java2d.opengl", "true")

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Self-Vault"
        ) {
            LaunchedEffect(window) {
                window.addComponentListener(object : ComponentAdapter() {
                    override fun componentResized(e: ComponentEvent?) {
                        window.revalidate()
                        window.repaint()
                    }
                })
            }
            App()
        }
    }
}