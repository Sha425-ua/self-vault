rootProject.name = "self-vault"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

include(
    "vault-crypto",
    "vault-domain",
    "vault-server",
    "vault-cli",
    "vault-desktop"
)