// Root project: only aggregates. Task names like `test`, `integrationTest`, `e2eTest` and `pitest`
// are resolved in every subproject that defines them, so `./gradlew test` runs all modules.

plugins {
    base
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.node.gradle) apply false
    alias(libs.plugins.pitest) apply false
}

val deployScript = layout.projectDirectory.file("deploy.sh").asFile

tasks.register<Exec>("stackUp") {
    group = "stack"
    description = "Builds and starts the production stack via docker compose and waits until it is healthy."
    commandLine(deployScript.absolutePath, "up")
}

tasks.register<Exec>("stackDown") {
    group = "stack"
    description = "Stops the production stack (volumes are kept)."
    commandLine(deployScript.absolutePath, "down")
}
