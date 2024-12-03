group = "org.coco.application"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dep.management)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infra:infra-jpa"))
    implementation(libs.spring.boot.core)
    implementation(libs.spring.tx)
}
