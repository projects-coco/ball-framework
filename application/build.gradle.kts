import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

group = "org.coco.application"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dep.management)
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.spring.boot.core)
    implementation(libs.spring.tx)
    implementation(libs.spring.boot.aop)
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
            artifactId = "application"
            version = rootVersion.toString()
        }
    }
}
