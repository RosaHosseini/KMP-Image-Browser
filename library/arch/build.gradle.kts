plugins {
    id("findr.kotlin.multiplatform.dynamic")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutinesCore)
        }
    }
}
