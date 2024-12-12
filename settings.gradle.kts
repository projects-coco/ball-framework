plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "ball-framework"
// PRESENTATION
include("presentation")
include("presentation:presentation-mvc")
findProject(":presentation:presentation-mvc")?.name = "presentation-mvc"

// application
include("application")

// DOMAIN
include("domain")

// INFRA
include("infra")

include("infra:infra-jpa")
findProject(":infra:infra-jpa")?.name = "infra-jpa"

include("infra:infra-mongodb")
findProject(":infra:infra-mongodb")?.name = "infra-mongodb"

include("infra:infra-redis")
findProject(":infra:infra-redis")?.name = "infra-redis"

include("infra:infra-spring-security")
findProject(":infra:infra-spring-security")?.name = "infra-spring-security"

include("infra:infra-auth-jpa")
findProject(":infra:infra-auth-jpa")?.name = "infra-auth-jpa"

// CORE
include("core-utils")

// ETC
include("sample-project")
