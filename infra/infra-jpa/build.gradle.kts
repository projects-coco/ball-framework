group = "org.coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    api(project(":domain"))
    api(libs.spring.boot.data.jpa)
    api(libs.spring.data.envers)
    api(libs.hypersistence.utils)
    api(libs.kotlin.jdsl.jpql.dsl)
    api(libs.kotlin.jdsl.jpql.render)
    api(libs.kotlin.jdsl.spring.data.jpa.support)

    testFixturesApi(libs.kotest.extensions.spring)
    testFixturesApi(libs.kotest.extensions.testcontainers)
    testFixturesApi(libs.spring.boot.test)
    testFixturesApi(libs.spring.boot.testcontainers)
    testFixturesApi(libs.testcontainers)
    testFixturesApi(libs.testcontainers.mariadb)
    testFixturesApi(libs.mariadb.client)
    testFixturesApi(testFixtures(project(":domain")))
}
