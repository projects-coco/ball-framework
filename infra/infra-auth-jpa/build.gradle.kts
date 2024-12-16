group = "com.github.project-coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
    annotation("jakarta.persistence.Entity")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infra:infra-jpa"))
    api(project(":infra:infra-auth-spring-security"))
}
