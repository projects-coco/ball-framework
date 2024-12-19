group = "org.coco.ball-framework"

dependencies {
    api(project(":core-utils"))
    api(libs.spring.data.core)
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
            artifact(tasks["testFixturesJar"])
        }
    }
}
