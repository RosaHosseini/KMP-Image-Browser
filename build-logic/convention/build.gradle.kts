import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

val javaVersion = JavaVersion.VERSION_18

group = "com.rosahoseeini.findr.buildlogic"

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = javaVersion.majorVersion
    }
}

dependencies {
    compileOnly("com.android.tools.build:gradle:${libs.versions.agp.get()}")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    compileOnly("org.jetbrains.compose:compose-gradle-plugin:${libs.versions.composeMultiplatform.get()}")

}

gradlePlugin {
    plugins {

        register("androidApplication") {
            id = "findr.android.application"
            implementationClass = "AndroidAppPlugin"
        }
        register("dynamicMultiplatformLibrary") {
            id = "findr.kotlin.multiplatform.dynamic"
            implementationClass = "DynamicMultiplatformPlugin"
        }
        register("nativeMultiplatformLibrary") {
            id = "findr.kotlin.multiplatform.native"
            implementationClass = "NativeMultiplatformPlugin"
        }
        register("composeMultiplatform") {
            id = "findr.kotlin.multiplatform.compose"
            implementationClass = "ComposeMultiplatformPlugin"
        }
    }
}