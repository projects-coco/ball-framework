group = "org.coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
}

dependencies {
    api(project(":domain"))
    api(project(":application"))
    api(libs.spring.boot.data.redis)
    api(libs.redis.redisson)
    api(libs.redis.redisson.spring.boot.starter)
}
