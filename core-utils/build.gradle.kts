import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

group = "org.coco.core.utils"

dependencies {
    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines.core)
    api(libs.arrow.core)
    api(libs.slf4j)
    implementation(libs.ulid)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jdk8)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)
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
            artifactId = "core-utils"
            version = rootVersion.toString()
        }
    }
}