import org.jetbrains.kotlin.konan.properties.Properties
import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.io.FileInputStream

group = "org.coco.presentation.mvc"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dep.management)
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))
    implementation(project(":infra:infra-auth-spring-security"))
    api(libs.spring.boot.mvc)
    api(libs.spring.aspects)
    api(libs.spring.boot.security)
    runtimeOnly(libs.spring.boot.actuator)
    developmentOnly(libs.spring.boot.devtools)
}

tasks {
    named<BootJar>("bootJar") {
        enabled = false
    }
}

val rootVersion = rootProject.version
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
localProperties.load(FileInputStream(localPropertiesFile))

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            group = "com.github.project-coco.ball-framework"
            artifactId = project.name
            version = rootVersion.toString()
        }
    }
}
