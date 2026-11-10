import com.github.gradle.node.npm.task.NpmTask

// Vue app, built and tested via npm. Gradle downloads its own Node.js (version in libs.versions.toml).

plugins {
    base
    alias(libs.plugins.node.gradle)
}

node {
    download = true
    version = libs.versions.nodejs.get()
    npmInstallCommand = "ci"
}

val sources = listOf("src", "index.html", "vite.config.ts", "tsconfig.json", "tsconfig.app.json", "tsconfig.node.json")

val npmBuild = tasks.register<NpmTask>("npmBuild") {
    group = "build"
    description = "Type-checks and builds the static assets into dist/."
    dependsOn(tasks.npmInstall)
    args = listOf("run", "build")
    inputs.files(sources)
    inputs.files("package.json", "package-lock.json")
    outputs.dir("dist")
}

val test = tasks.register<NpmTask>("test") {
    group = "verification"
    description = "Runs the frontend unit tests (Vitest)."
    dependsOn(tasks.npmInstall)
    args = listOf("run", "test")
    inputs.files(sources)
    inputs.files("package.json", "package-lock.json")
    outputs.upToDateWhen { false }
}

tasks.assemble {
    dependsOn(npmBuild)
}

tasks.check {
    dependsOn(test)
}

tasks.clean {
    delete("dist")
}
