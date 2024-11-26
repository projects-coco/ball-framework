import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinter)
}

group = "org.coco"
version = "1.0-SNAPSHOT"

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
            deps.plugins.kotlinter
                .get()
                .pluginId,
        )
    }

    java.sourceCompatibility = JavaVersion.VERSION_21
    java.targetCompatibility = JavaVersion.VERSION_21

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(deps.kotlin.reflect)
        implementation(deps.kotlinx.coroutines.core)
        implementation(deps.kotlinx.coroutines.reactor)
        implementation(deps.arrow.core)
        testImplementation(deps.bundles.kotest)
        testImplementation(deps.mockk)
        testImplementation(kotlin("test"))
    }

    tasks {
        formatKotlinMain {
            exclude { it.file.path.contains("generated") }
        }
        lintKotlinMain {
            dependsOn("formatKotlinMain")
            exclude { it.file.path.contains("generated") }
        }
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
