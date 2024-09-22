plugins {
    id("findr.kotlin.multiplatform.native")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.data.db)
            implementation(projects.domain.bookmark)
            implementation(projects.domain.model)
            implementation(projects.library.coroutine)
            implementation(libs.coroutinesCore)
            implementation(project.dependencies.platform(libs.koinBom))
            implementation(libs.koinCore)
        }

        commonTest.dependencies {
            implementation(libs.bundles.testCore)
        }

        androidUnitTest.dependencies {
            implementation(libs.test.mockk)
        }
    }
}
