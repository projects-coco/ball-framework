import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "org.coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dep.management)
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))
    implementation(project(":infra:infra-auth-spring-security"))
    api(libs.spring.boot.mvc)
    api(libs.spring.aspects)
    api(libs.spring.boot.security)
    api(libs.prometheus)
    api(libs.spring.boot.actuator)
    developmentOnly(libs.spring.boot.devtools)
}

tasks {
    named<BootJar>("bootJar") {
        enabled = false
    }
}
