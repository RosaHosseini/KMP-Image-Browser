plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rosahosseini.findr.library.core"
}

dependencies {
    implementation(libs.lifecycleViewModel)
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)
}
