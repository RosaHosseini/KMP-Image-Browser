plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rosahosseini.findr.data.search"
}

dependencies {
    implementation(project(":data:common"))
    implementation(project(":data:db"))
    implementation(project(":domain:search"))
    implementation(project(":domain:model"))
    implementation(project(":library:coroutine"))
    implementation(libs.coroutinesAndroid)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)

    testImplementation(libs.bundles.testCore)
}
