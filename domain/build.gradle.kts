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
