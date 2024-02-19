plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rosahosseini.findr.data.repository"
}

dependencies {
    implementation(project(":data:remote"))
    implementation(project(":data:local"))
    implementation(project(":domain:model"))
    implementation(project(":library:core"))
    implementation(libs.coroutinesAndroid)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)

    testImplementation(libs.bundles.testCore)
}
