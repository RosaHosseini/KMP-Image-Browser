plugins {
    alias(libs.plugins.androidLibrary)
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
    implementation(project(":library:core"))
    implementation(project(":library:ui"))
    implementation(project(":domain:model"))
    implementation(platform(libs.composeBom))
    implementation(libs.composeNavigation)
    implementation(libs.composeMaterial3)
}
