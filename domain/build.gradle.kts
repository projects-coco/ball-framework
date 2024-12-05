group = "org.coco.domain"

dependencies {
    api(project(":core-utils"))
    api(libs.arrow.core)
    api(libs.spring.data.core)
    implementation(libs.ulid)
}
