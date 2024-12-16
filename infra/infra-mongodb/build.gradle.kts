import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

group = "org.coco.infra.mongodb"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
}

dependencies {
    api(project(":domain"))
    api(libs.spring.boot.data.mongodb)
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
