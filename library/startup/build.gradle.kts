plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rosahosseini.findr.library.startup"
}

dependencies {
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)
}
