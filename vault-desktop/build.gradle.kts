plugins {
    kotlin("jvm") version "2.1.0"
    id("org.jetbrains.compose") version "1.7.3"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
}

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":vault-domain"))
    implementation(project(":vault-crypto"))
    implementation(project(":vault-client"))

    // Compose Desktop:
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation("androidx.graphics:graphics-shapes:1.0.1")

    // Jackson JSON parser:
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")

    // Logger
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")

    // Coroutine
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
}

compose.desktop {
    application {
        mainClass = "com.selfvault.desktop.MainKt"
    }
}

tasks.withType<JavaExec> {
    environment("_JAVA_AWT_WM_NONREPARENTING", "1")
    environment("LD_LIBRARY_PATH", "/run/opengl-driver/lib:/run/current-system/sw/share/nix-ld/lib")

    systemProperty("vault.debug", System.getProperty("vault.debug", "false"))
}
