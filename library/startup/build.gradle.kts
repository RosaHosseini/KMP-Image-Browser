plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rosahosseini.findr.library.startup"
}

dependencies {
    implementation("com.google.auto.value:auto-value-annotations:1.10.1")
    ksp("com.google.auto.value:auto-value:1.10.1")
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)
}