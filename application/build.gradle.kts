import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

group = "org.coco.application"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dep.management)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infra:infra-redis"))
    implementation(libs.spring.boot.core)
    implementation(libs.spring.tx)
    implementation(libs.spring.boot.aop)
}


val rootVersion = rootProject.version
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
localProperties.load(FileInputStream(localPropertiesFile))

publishing {
    repositories {
        maven {
            val releasesRepoUrl = uri(localProperties["coco.repo.url.release"] as String)
            val snapshotsRepoUrl = uri(localProperties["coco.repo.url.snapshot"] as String)
            credentials {
                username = localProperties["coco.repo.username"] as String
                password = localProperties["coco.repo.password"] as String
            }
            url = if (rootVersion.toString().endsWith("RELEASE")) releasesRepoUrl else snapshotsRepoUrl
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            groupId = "org.coco"
            artifactId = "application"
            version = rootVersion.toString()
        }
    }
}