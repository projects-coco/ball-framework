plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "ball-framework"
// PRESENTATION
include("presentation")

// DOMAIN
include("domain")

// INFRA
include("infra")
include("infra:infra-jpa")
findProject(":infra:infra-jpa")?.name = "infra-jpa"
include("sample-project")
include("presentation:presentation-mvc")
findProject(":presentation:presentation-mvc")?.name = "presentation-mvc"
include("application")
