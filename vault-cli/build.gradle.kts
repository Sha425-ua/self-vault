plugins {
    application
}

application {
    mainClass.set("com.selfvault.cli.VaultCli")
}

dependencies {
    implementation(project(":vault-crypto"))
    implementation(project(":vault-domain"))
    implementation("info.picocli:picocli:4.7.6")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
