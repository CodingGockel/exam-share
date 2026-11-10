plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.pitest)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.flyway)
    implementation(libs.flyway.postgresql)
    runtimeOnly(libs.postgresql)
}

testing {
    suites {
        // Unit tests: src/test/java, run by `./gradlew test`
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
            dependencies {
                implementation(platform(libs.spring.boot.bom))
                implementation(libs.spring.boot.starter.test)
                implementation(libs.spring.boot.starter.webmvc.test)
                implementation(libs.assertj)
                implementation(libs.jqwik)
            }
        }

        // Integration tests against a real PostgreSQL (Testcontainers): src/integrationTest/java
        register<JvmTestSuite>("integrationTest") {
            useJUnitJupiter()
            dependencies {
                implementation(project())
                implementation(platform(libs.spring.boot.bom))
                implementation(libs.spring.boot.starter.test)
                implementation(libs.spring.boot.starter.webmvc.test)
                implementation(libs.spring.boot.starter.data.jpa.test)
                implementation(libs.spring.boot.testcontainers)
                implementation(libs.testcontainers.junit.jupiter)
                implementation(libs.testcontainers.postgresql)
                implementation(libs.assertj)
                implementation(libs.jqwik)
            }
            targets.all {
                testTask.configure {
                    shouldRunAfter(tasks.named("test"))
                    description = "Runs the backend integration tests (Docker required)."
                    group = "verification"
                }
            }
        }
    }
}

// The integration tests see the same dependencies as the application itself
configurations.named("integrationTestImplementation") {
    extendsFrom(configurations.implementation.get())
}
configurations.named("integrationTestRuntimeOnly") {
    extendsFrom(configurations.runtimeOnly.get())
}

tasks.named("check") {
    dependsOn(tasks.named("integrationTest"))
}

// Fixed name for the executable jar (used by backend/Dockerfile)
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName = "app.jar"
}

tasks.withType<Test>().configureEach {
    // silence JVM warnings (Testcontainers/JNA native access, class data sharing) in the test output
    jvmArgs("--enable-native-access=ALL-UNNAMED", "-Xshare:off")
    testLogging {
        events("failed", "skipped")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

pitest {
    pitestVersion = libs.versions.pitest.tool.get()
    junit5PluginVersion = libs.versions.pitest.junit5.get()
    targetClasses = setOf("de.pruefprot.*")
    excludedClasses = setOf("de.pruefprot.PruefprotApplication")
    outputFormats = setOf("HTML")
    timestampedReports = false
    threads = 4
}
