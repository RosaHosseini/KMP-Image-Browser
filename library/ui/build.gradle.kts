plugins {
    id("findr.kotlin.multiplatform.native")
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.composeActivity)
        }

        commonMain.dependencies {
            implementation(projects.domain.model)
            implementation(projects.library.imageloader)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.animation)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.immutableCollections)
        }

        commonTest.dependencies {
            implementation(libs.bundles.testCore)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.rosahosseini.findr.library.ui"
    generateResClass = auto
}
