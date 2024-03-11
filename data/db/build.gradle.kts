plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rosahosseini.findr.data.db"
}

dependencies {
    implementation(project(":domain:model"))
    implementation(libs.roomKtx)
    ksp(libs.roomCompiler)
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)
    testImplementation(libs.bundles.testCore)
}
