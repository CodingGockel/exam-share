plugins {
    // Downloads the Java toolchain (JDK 25) automatically if it is not installed locally
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "pruefprot"

include("backend", "frontend", "legacy", "e2e")
