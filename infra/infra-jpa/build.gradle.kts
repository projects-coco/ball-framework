group = "org.coco.ball-framework"

plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotations("jakarta.persistence.MappedSuperclass")
}

dependencies {
    api(project(":domain"))
    api(libs.spring.boot.data.jpa)
    api(libs.spring.data.envers)
    api(libs.hypersistence.utils)
    api(libs.kotlin.jdsl.jpql.dsl)
    api(libs.kotlin.jdsl.jpql.render)
    api(libs.kotlin.jdsl.spring.data.jpa.support)
}
