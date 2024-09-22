plugins {
    id("findr.kotlin.multiplatform.native")
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

kotlin {

    sourceSets {
        androidMain.dependencies {
            implementation(libs.koinAndroid)
        }

        commonMain.dependencies {
            implementation(projects.domain.model)
            implementation(projects.library.coroutine)
            implementation(libs.datetime)
            implementation(libs.roomRuntime)
            implementation(libs.sqliteBundled)
            implementation(libs.sqlite)
            implementation(project.dependencies.platform(libs.koinBom))
            implementation(libs.koinCore)
        }
    }
}

dependencies {
    add("kspAndroid", libs.roomCompiler)
    add("kspIosSimulatorArm64", libs.roomCompiler)
    add("kspIosX64", libs.roomCompiler)
    add("kspIosArm64", libs.roomCompiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
