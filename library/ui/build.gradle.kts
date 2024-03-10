plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.rosahosseini.findr.library.ui"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":domain:model"))
    implementation(platform(libs.composeBom))
    implementation(libs.composeMaterial3)
    implementation(libs.composeNavigation)
    implementation(libs.immutableCollections)
    implementation(libs.composeTooling)
    implementation(libs.composeCoil)
}
