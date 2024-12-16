group = "org.coco.infra.redis"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
}

dependencies {
    api(project(":domain"))
    api(project(":application"))
    api(libs.spring.boot.data.redis) {
        implementation(libs.redis.lettuce)
    }
    api(libs.redis.redisson)
}
