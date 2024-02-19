plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":domain:model"))
    implementation(libs.coroutinesCore)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.hiltCore)
    ksp(libs.hiltCompiler)
}
