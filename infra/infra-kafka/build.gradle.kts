group = "org.coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dep.management)
}

dependencies {
    api(project(":domain"))
    implementation(libs.spring.boot.core)
    implementation(libs.spring.boot.kafka)
}
