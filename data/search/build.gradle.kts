plugins {
    id("findr.kotlin.multiplatform.native")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.data.common)
            implementation(projects.data.db)
            implementation(projects.domain.search)
            implementation(projects.domain.model)
            implementation(projects.library.coroutine)
            implementation(libs.kotlinxJsonSerialization)

            implementation(libs.coroutinesCore)
            implementation(libs.datetime)
            implementation(libs.ktorCore)
            implementation(project.dependencies.platform(libs.koinBom))
            implementation(libs.koinCore)
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
