plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(project(":domain:model"))
    implementation(libs.gson)
}
