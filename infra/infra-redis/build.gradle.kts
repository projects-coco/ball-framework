group = "org.coco.infra.redis"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
}

dependencies {
    api(project(":domain"))
    api(libs.spring.boot.data.redis) {
        implementation("io.lettuce:lettuce-core:6.5.1.RELEASE")
    }
}
