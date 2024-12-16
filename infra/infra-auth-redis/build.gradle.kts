group = "org.coco.infra.auth.redis"

plugins {
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infra:infra-redis"))
    api(project(":infra:infra-auth-spring-security"))
}
