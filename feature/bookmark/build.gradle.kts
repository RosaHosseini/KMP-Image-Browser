plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.rosahosseini.findr.feature.bookmark"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":library:coroutine"))
    implementation(project(":library:arch"))
    implementation(project(":library:ui"))
    implementation(project(":domain:model"))
    implementation(project(":domain:bookmark"))
    implementation(platform(libs.composeBom))
    implementation(libs.composeRuntime)
    implementation(libs.immutableCollections)
    implementation(libs.composeMaterial3)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.composeNavigation)
    implementation(libs.composeTooling)
    implementation(libs.lifecycleViewModel)
    implementation(libs.lifecycleCompose)
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)

    testImplementation(libs.bundles.testCore)
}
