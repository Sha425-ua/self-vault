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

    // Jackson JSON parser:
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")

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
}
