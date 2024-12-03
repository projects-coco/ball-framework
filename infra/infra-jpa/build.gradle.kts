group = "org.coco.infra.jpa"

plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
    annotation("jakarta.persistence.Entity")
}

dependencies {
    implementation(project(":domain"))
    api(libs.spring.boot.data.jpa)
    api(libs.spring.data.envers)
}
