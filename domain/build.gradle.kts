group = "org.coco.domain"

dependencies {
    implementation(libs.ulid)
    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.reactor)
    api(libs.arrow.core)
    api(libs.slf4j)
    api(libs.spring.data.core)
}
