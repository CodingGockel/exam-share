# syntax=docker/dockerfile:1
# Build context: repository root (the Gradle build needs the root build files)

FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /workspace

# Gradle wrapper and build scripts first, so dependency downloads are cached between builds
COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle gradle
COPY backend/build.gradle.kts backend/
COPY legacy/build.gradle.kts legacy/
COPY e2e/build.gradle.kts e2e/
COPY frontend/build.gradle.kts frontend/
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon -q :backend:dependencies > /dev/null

COPY backend/src backend/src
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon :backend:bootJar


FROM eclipse-temurin:25-jre-alpine
RUN addgroup -S app && adduser -S -G app app \
    && mkdir -p /app/logs && chown app:app /app/logs
WORKDIR /app
COPY --from=build /workspace/backend/build/libs/app.jar app.jar
ENV PRUEFPROT_LOG_FILE=/app/logs/server-log
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]
