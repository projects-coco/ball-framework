group = "org.coco.ball-framework"

dependencies {
    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.reactor)
    api(libs.arrow.core)
    api(libs.slf4j)
    api(libs.javers)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jdk8)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.arrow.jackson.module)
    implementation(libs.ulid)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/projects-coco/ball-framework")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("publish") {
            group = "org.coco.ball-framework"
            artifactId = project.name
            version = rootProject.version.toString()
            from(components["java"])
            artifact(tasks["sourcesJar"])
        }
    }
}
