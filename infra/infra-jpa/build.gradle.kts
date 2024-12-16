import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

group = "org.coco.infra.jpa"

plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotations("jakarta.persistence.MappedSuperclass")
}

dependencies {
    api(project(":domain"))
    api(libs.spring.boot.data.jpa)
    api(libs.spring.data.envers)
    api(libs.hypersistence.utils)
    api(libs.kotlin.jdsl.jpql.dsl)
    api(libs.kotlin.jdsl.jpql.render)
    api(libs.kotlin.jdsl.spring.data.jpa.support)
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
            group = "com.github.project-coco.ball-framework"
            artifactId = project.name
            version = rootVersion.toString()
        }
    }
}
