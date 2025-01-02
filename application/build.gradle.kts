group = "org.coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dep.management)
}

dependencies {
    api(project(":domain"))
    api(libs.spring.boot.core)
    api(libs.spring.tx)
    api(libs.spring.boot.aop)
}

configure<PublishingExtension> {
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
