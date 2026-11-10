// Small standalone library with JUnit 4 tests (on purpose, for the JUnit 4 -> 5 migration demo).
// Do not migrate unless explicitly asked to.

plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

dependencies {
    testImplementation(libs.junit4)
    testImplementation(libs.hamcrest.library.legacy)
}

tasks.test {
    useJUnit()
}
