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
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            group = "com.github.project-coco.ball-framework"
            artifactId = "core-utils"
            version = rootVersion.toString()
        }
    }
}
