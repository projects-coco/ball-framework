group = "org.coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dep.management)
}

allOpen {
    annotation("jakarta.persistence.Entity")
}

dependencies {
    implementation(project(":presentation:presentation-mvc"))
    implementation(project(":application"))
    implementation(project(":domain"))
    implementation(project(":infra:infra-jpa"))
    implementation(project(":infra:infra-mongodb"))
    implementation(project(":infra:infra-redis"))
    implementation(project(":infra:infra-auth-redis"))
    implementation(project(":infra:infra-kafka"))
    runtimeOnly(libs.mariadb.client)
}
