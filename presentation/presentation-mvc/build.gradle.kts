group = "org.coco.presentation.mvc"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dep.management)
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))
    api(libs.spring.boot.mvc)
    api(libs.spring.aspects)
    runtimeOnly(libs.spring.boot.actuator)
    developmentOnly(libs.spring.boot.devtools)
}
