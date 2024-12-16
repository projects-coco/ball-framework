import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.maven.publish)
}

group = "com.github.project-coco"
version = "0.0.1-RELEASE"

repositories {
    mavenCentral()
}

val deps = libs

allprojects {
    val localPropertiesFile = rootProject.file("local.properties")
    val localProperties = Properties()
    localProperties.load(FileInputStream(localPropertiesFile))

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
    }

    java.sourceCompatibility = JavaVersion.VERSION_21
    java.targetCompatibility = JavaVersion.VERSION_21

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(deps.bundles.kotest)
        testImplementation(deps.mockk)
        testImplementation(kotlin("test"))
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

    publishing {
        publications {
            create<MavenPublication>("publishToMavenLocal") {
                group = "com.github.project-coco.ball-framework"
                artifactId = project.name
                version = rootProject.version.toString()
                from(components["java"])
                artifact(tasks["sourcesJar"])
            }
        }
    }
}
