group = "org.coco.infra.spring.security"

plugins {
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.spring.security.crypto)
    implementation(libs.jwt)
}
