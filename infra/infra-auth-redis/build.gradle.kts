import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

group = "org.coco.infra.auth.redis"

plugins {
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infra:infra-redis"))
    api(project(":infra:infra-auth-spring-security"))
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
