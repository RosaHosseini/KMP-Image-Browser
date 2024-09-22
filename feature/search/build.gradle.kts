
plugins {
    id("findr.kotlin.multiplatform.native")
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.library.startup)
            implementation(projects.library.coroutine)
            implementation(projects.library.ui)
            implementation(projects.library.arch)
            implementation(projects.domain.model)
            implementation(projects.domain.search)
            implementation(projects.feature.bookmark)

            implementation(compose.foundation)
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)
            implementation(libs.immutableCollections)
            implementation(libs.composeNavigation)
            implementation(libs.lifecycleCompose)
            implementation(libs.lifecycleViewModelCompose)
            implementation(project.dependencies.platform(libs.koinBom))
            implementation(libs.koinComposeViewmodel)
        }

        androidMain.dependencies {
            implementation(libs.workManagerRuntime)
            implementation(libs.koinWorkManager)
        }

        commonTest.dependencies {
            implementation(libs.bundles.testCore)
        }

        androidUnitTest.dependencies {
            implementation(libs.test.mockk)
            implementation(libs.test.kluent)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.rosahosseini.findr.feature.search"
    generateResClass = auto
}
