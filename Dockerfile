FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle ./gradle

COPY vault-crypto/build.gradle.kts ./vault-crypto/
COPY vault-domain/build.gradle.kts ./vault-domain/
COPY vault-server/build.gradle.kts ./vault-server/
COPY vault-cli/build.gradle.kts ./vault-cli/

RUN ./gradlew dependencies --no-daemon || true

COPY vault-crypto/src ./vault-crypto/src
COPY vault-domain/src ./vault-domain/src
COPY vault-server/src ./vault-server/src
COPY vault-cli/src ./vault-cli/src

RUN ./gradlew :vault-server:bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S vaultgroup && adduser -S vaultuser -G vaultgroup

RUN mkdir -p /data && chown -R vaultuser:vaultgroup /data /app

COPY --from=builder --chown=vaultuser:vaultgroup /app/vault-server/build/libs/*.jar app.jar

USER vaultuser

EXPOSE 8080

VOLUME ["/data"]

ENV VAULT_DATA_DIR="/data"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]