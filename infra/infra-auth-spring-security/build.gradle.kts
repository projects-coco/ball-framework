group = "org.coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.spring.security.crypto) {
        implementation(libs.bouncycastle.crypto)
    }
    api(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)
}
