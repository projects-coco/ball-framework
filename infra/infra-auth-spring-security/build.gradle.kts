group = "com.github.project-coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.spring.security.crypto) {
        implementation(libs.bouncycastle.crypto)
    }
    api(libs.jwt)
}
