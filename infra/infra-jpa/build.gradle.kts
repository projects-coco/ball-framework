group = "org.coco.infra.jpa"

plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(project(":domain"))
    api(libs.spring.boot.data.jpa)
}
