group = "org.coco.infra.auth.security"

plugins {
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.spring.security.crypto) {
        implementation(libs.bouncycastle.crypto)
    }
    implementation(libs.jwt)
}
