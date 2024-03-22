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
    implementation(project(":library:coroutine"))
    implementation(project(":library:arch"))
    implementation(project(":library:ui"))
    implementation(project(":domain:model"))
    implementation(project(":domain:search"))
    implementation(project(":feature:bookmark"))
    implementation(platform(libs.composeBom))
    implementation(libs.lifecycleCompose)
    implementation(libs.composeNavigation)
    implementation(libs.immutableCollections)
    implementation(libs.composeMaterial3)
    implementation(libs.composeFlowLayout)
    implementation(libs.composeTooling)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.workManagerRuntime)
    implementation(libs.workManagerHilt)
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)

    testImplementation(libs.bundles.testCore)
}
