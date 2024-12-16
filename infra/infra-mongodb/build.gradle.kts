group = "org.coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
}

dependencies {
    api(project(":domain"))
    api(libs.spring.boot.data.mongodb)
}
