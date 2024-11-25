group = "org.coco.domain"

plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
    annotation("jakarta.persistence.Entity")
}

dependencies {
    implementation(libs.ulid)
    api(libs.slf4j)
    api(libs.jpa)
    api(libs.spring.data.core)
    api(libs.spring.data.envers)
}
