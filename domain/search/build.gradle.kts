plugins {
    id("findr.kotlin.multiplatform.dynamic")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.model)
            implementation(libs.coroutinesCore)
        }
    }
}
