// End-to-end tests with Playwright for Java against a running stack.
// Start the stack first (`./gradlew stackUp`), then run `./gradlew e2eTest`.
// Target URL: environment variable BASE_URL (default http://localhost).

plugins {
    java
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

testing {
    suites {
        register<JvmTestSuite>("e2eTest") {
            useJUnitJupiter()
            dependencies {
                implementation(platform(libs.spring.boot.bom))
                implementation(libs.playwright)
                implementation(libs.assertj)
            }
            targets.all {
                testTask.configure {
                    description = "Runs the Playwright end-to-end tests against BASE_URL (default http://localhost)."
                    group = "verification"
                    environment("BASE_URL", System.getenv("BASE_URL") ?: "http://localhost")
                    // always run against the live stack, never take results from the cache
                    outputs.upToDateWhen { false }
                    testLogging {
                        events("failed", "skipped")
                        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                    }
                }
            }
        }
    }
}

tasks.register<JavaExec>("installPlaywrightBrowsers") {
    group = "verification"
    description = "Downloads the Chromium build used by Playwright (also happens automatically on first use)."
    classpath = sourceSets["e2eTest"].runtimeClasspath
    mainClass = "com.microsoft.playwright.CLI"
    args("install", "chromium")
}
