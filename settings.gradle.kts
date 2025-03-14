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

include("infra:infra-auth-spring-security")
findProject(":infra:infra-auth-spring-security")?.name = "infra-auth-spring-security"

include("infra:infra-auth-jpa")
findProject(":infra:infra-auth-jpa")?.name = "infra-auth-jpa"

include("infra:infra-auth-redis")
findProject(":infra:infra-auth-redis")?.name = "infra-auth-redis"

include("infra:infra-kafka")
findProject(":infra:infra-kafka")?.name = "infra-kafka"

// CORE
include("core-utils")

// ETC
include("sample-project")
