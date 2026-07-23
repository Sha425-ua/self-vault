plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":vault-domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    implementation("org.bouncycastle:bcprov-jdk18on:1.77")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.77")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
