package com.selfvault.desktop.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

class ConfigLoader(
    file: File = AppPaths.configFile
) {
    var config: AppConfig

    init {
        var mapper = ObjectMapper().registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .enable(SerializationFeature.INDENT_OUTPUT)

        file.parentFile?.mkdirs()

        if (file.exists()) {
            config = mapper.readValue(file, AppConfig::class.java)
        } else {
            val defaultConfig = AppConfig()
            mapper.writeValue(file, defaultConfig)
            config = defaultConfig
        }
    }
}