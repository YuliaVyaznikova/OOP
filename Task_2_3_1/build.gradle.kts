import org.gradle.testing.jacoco.tasks.JacocoReport
plugins {
    id("java")
    id("jacoco")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "ru.nsu.kolodina.snake"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)

    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("org.jetbrains:annotations:16.0.2")
    implementation("org.openjdk.jmh:jmh-core:1.36")
    implementation ("org.json:json:20171018")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")

}

tasks.test {
    useJUnitPlatform()
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
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("ru.nsu.vyaznikova.MainApp")
}