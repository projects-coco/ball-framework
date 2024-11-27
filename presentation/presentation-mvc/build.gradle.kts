group = "org.coco.presentation.mvc"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dep.management)
}

dependencies {
    api(libs.spring.boot.mvc)
    runtimeOnly(libs.spring.boot.actuator)
}
