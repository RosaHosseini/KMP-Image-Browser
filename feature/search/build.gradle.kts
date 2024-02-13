plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rosahosseini.findr.feature.search"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":library:startup"))
    implementation(project(":library:core"))
    implementation(project(":library:ui"))
    implementation(project(":domain:model"))
    implementation(project(":data:repository"))
    implementation(platform(libs.composeBom))
    implementation(libs.lifecycleCompose)
    implementation(libs.composeNavigation)
    implementation(libs.composeMaterial3)
    implementation(libs.composeFlowLayout)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.hiltAndroid)
    implementation(libs.workManagerRuntime)
    implementation(libs.workManagerHilt)
    ksp(libs.hiltCompiler)
}
