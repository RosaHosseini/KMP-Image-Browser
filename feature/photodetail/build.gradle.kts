plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.rosahosseini.findr.feature.photodetail"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":library:coroutine"))
    implementation(project(":library:ui"))
    implementation(project(":domain:model"))
    implementation(platform(libs.composeBom))
    implementation(libs.lifecycleCompose)
    implementation(libs.composeTooling)
    implementation(libs.composeNavigation)
    implementation(libs.composeMaterial3)
}
