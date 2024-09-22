plugins {
    id("findr.kotlin.multiplatform.native")
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.library.coroutine)
            implementation(projects.library.ui)
            implementation(projects.library.imageloader)
            implementation(projects.domain.model)
            implementation(compose.foundation)
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.composeNavigation)
        }
    }
}
