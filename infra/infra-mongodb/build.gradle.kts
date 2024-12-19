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
    testFixturesApi(libs.kotest.extensions.spring)
    testFixturesApi(libs.kotest.extensions.testcontainers)
    testFixturesApi(libs.spring.boot.test)
    testFixturesApi(libs.spring.boot.testcontainers)
    testFixturesApi(libs.testcontainers)
    testFixturesApi(libs.testcontainers.mongodb)
    testFixturesApi(libs.mongodb)
    testFixturesApi(testFixtures(project(":domain")))
}
