plugins {
    application
}

application {
    mainClass.set("com.selfvault.cli.VaultCLI")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.selfvault.cli.VaultCLI"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/*.sf", "META-INF/*.dsa", "META-INF/*.rsa", "META-INF/INDEX.LIST")
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

dependencies {
    implementation(project(":vault-crypto"))
    implementation(project(":vault-domain"))
    implementation("info.picocli:picocli:4.7.6")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
