plugins {
    id("findr.kotlin.multiplatform.dynamic")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.model)
            implementation(libs.kotlinxJsonSerialization)
            implementation(libs.ktorCore)
            implementation(project.dependencies.platform(libs.koinBom))
            implementation(libs.koinCore)
        }
    }
}
