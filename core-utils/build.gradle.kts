group = "org.coco.core.utils"

dependencies {
    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines.core)
    api(libs.arrow.core)
    api(libs.slf4j)
    implementation(libs.ulid)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jdk8)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)
}
