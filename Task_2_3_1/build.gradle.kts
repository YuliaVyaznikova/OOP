import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.api.tasks.Copy

plugins {
    id("java")
    id("jacoco")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "ru.nsu.vyaznikova"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
    test {
        java {
            srcDirs("src/test/java")
        }
        resources {
            srcDirs("src/test/resources")
        }
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    implementation("org.jetbrains:annotations:24.1.0")
    implementation("org.openjdk.jmh:jmh-core:1.37")
    implementation("org.json:json:20240303")
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.named<JacocoReport>("jacocoTestReport") {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.withType<JavaExec> {
    doFirst {
        jvmArgs = listOf(
            "--module-path", configurations.runtimeClasspath.get().asPath,
            "--add-modules", "javafx.controls,javafx.fxml"
        )
    }
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("ru.nsu.vyaznikova.MainApp")
}