
plugins {
    id("findr.kotlin.multiplatform.native")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.data.common)
            implementation(projects.domain.model)

            implementation(libs.bundles.ktor)
            implementation(project.dependencies.platform(libs.koinBom))
            implementation(libs.koinCore)
        }

        androidMain.dependencies {
            implementation(libs.ktorOkhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktorDarwin)
        }
    }
}
