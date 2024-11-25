group = "org.coco.example"

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
    implementation(project(":domain"))
    implementation(project(":infra:infra-jpa"))

    implementation(libs.spring.boot.mvc)
    runtimeOnly(libs.spring.boot.actuator)
    runtimeOnly(libs.mariadb)
}
