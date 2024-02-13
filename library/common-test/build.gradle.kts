plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.rosahosseini.findr.library.common-test"
}

dependencies {
    implementation(project(":library:core"))
    implementation(libs.bundles.testCore)
}