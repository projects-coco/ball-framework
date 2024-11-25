group = "org.coco.infra.jpa"

plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    api(libs.spring.boot.data.jpa)
}
