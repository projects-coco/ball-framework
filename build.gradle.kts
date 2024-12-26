import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.java.test.fixtures)
}

group = "com.github.project-coco"
version = "1.0.2-RELEASE"

repositories {
    mavenCentral()
}

val deps = libs

allprojects {
    apply {
        plugin("java")
        plugin(
            deps.plugins.kotlin.jvm
                .get()
                .pluginId,
        )
        plugin(
            deps.plugins.maven.publish
                .get()
                .pluginId,
        )
        plugin("java-test-fixtures")
    }

    java.sourceCompatibility = JavaVersion.VERSION_21
    java.targetCompatibility = JavaVersion.VERSION_21
    java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(deps.bundles.kotest)
        testImplementation(deps.mockk)
        testImplementation(kotlin("test"))
        testFixturesImplementation(deps.bundles.kotest)
    }
    task<Jar>("sourcesJar") {
        enabled = true
        archiveClassifier.set("sources")
        from(sourceSets.getByName("main").allSource)
    }

    tasks {
        test {
            useJUnitPlatform()
        }
        compileKotlin {
            compilerOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict", "-Xcontext-receivers")
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }
    }
}
