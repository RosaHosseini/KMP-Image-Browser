plugins {
    id("findr.kotlin.multiplatform.native")
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.foundation)
            implementation(compose.components.resources)

            implementation(libs.coil)
            implementation(libs.coilCompose)
            implementation(libs.coilNetworkKtor)
        }
        androidMain.dependencies {
            implementation(libs.ktorOkhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktorDarwin)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.rosahosseini.findr.library.imageloader"
    generateResClass = auto
}
