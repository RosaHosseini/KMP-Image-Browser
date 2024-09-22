plugins {
    id("findr.kotlin.multiplatform.native")
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.koinBom))
            implementation(libs.coroutinesCore)
            implementation(libs.koinCore)
        }
    }
}
