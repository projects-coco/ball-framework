import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

group = "org.coco.domain"

dependencies {
    api(project(":core-utils"))
    api(libs.spring.data.core)
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
            artifactId = "domain"
            version = rootVersion.toString()
        }
    }
}
