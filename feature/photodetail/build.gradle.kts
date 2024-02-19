plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.rosahosseini.findr.feature.photodetail"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":library:coroutine"))
    implementation(project(":library:ui"))
    implementation(project(":domain:model"))
    implementation(platform(libs.composeBom))
    implementation(libs.lifecycleCompose)
    implementation(libs.composeNavigation)
    implementation(libs.composeMaterial3)
}
