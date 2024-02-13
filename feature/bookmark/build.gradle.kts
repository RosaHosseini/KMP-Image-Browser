plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rosahosseini.findr.feature.bookmark"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":library:core"))
    implementation(project(":library:ui"))
    implementation(project(":domain:model"))
    implementation(project(":data:repository"))
    implementation(platform(libs.composeBom))
    implementation(libs.composeRuntime)
    implementation(libs.composeMaterial3)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.composeNavigation)
    implementation(libs.composeTooling)
    implementation(libs.lifecycleCompose)
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)
}