plugins {
    id("findr.kotlin.multiplatform.dynamic")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutinesCore)
            implementation(project.dependencies.platform(libs.koinBom))
            implementation(libs.koinCore)
        }
    }
}
