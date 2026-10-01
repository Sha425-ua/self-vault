package com.selfvault.desktop.config

import java.io.File

object AppPaths {
    val configDir: File
        get() {
            val userHome = System.getProperty("user.home")
            val osName = System.getProperty("os.name").lowercase()

            val dir = when {
                osName.contains("win") -> {
                    val appData = System.getenv("APPDATA") ?: userHome
                    File(appData, "self-vault")
                }
                osName.contains("mac") -> {
                    File("$userHome/Library/Application Support/self-vault")
                }
                else -> {
                    val xdgConfig = System.getenv("XDG_CONFIG_HOME")
                    if (!xdgConfig.isNullOrBlank()) {
                        File(xdgConfig, "self-vault")
                    } else {
                        File("$userHome/.config/self-vault")
                    }
                }
            }
            dir.mkdirs()

            return dir
        }
    val configFile: File
        get() = File(configDir, "config.json")
}