group = "com.github.project-coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dep.management)
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.spring.boot.core)
    implementation(libs.spring.tx)
    implementation(libs.spring.boot.aop)
}
