import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

val apikeyPropertiesFile: File = rootProject.file("apikey.properties")
val apikeyProperties = Properties()
if (apikeyPropertiesFile.exists()) {
    apikeyPropertiesFile.inputStream().use(apikeyProperties::load)
}

android {
    namespace = "com.rosahosseini.findr.app"

    defaultConfig {
        applicationId = "com.rosahosseini.findr"
        buildConfigField(
            "String",
            "FLICKR_API_KEY",
            apikeyProperties["FLICKR_KEY"] as String? ?: ""
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging {
        resources.excludes += setOf(
            "META-INF/LICENSE.md",
            "META-INF/LICENSE-notice.md",
            "META-INF/**previous-compilation-data.bin",
            "DebugProbesKt.bin",
            "META-INF/proguard/*",
            "/*.properties",
            "META-INF/*.properties"
        )
    }
}

dependencies {
    implementation(project(":library:ui"))
    implementation(project(":library:startup"))
    implementation(project(":data:network"))
    implementation(project(":data:search"))
    implementation(project(":data:bookmark"))
    implementation(project(":domain:model"))
    implementation(project(":feature:search"))
    implementation(project(":feature:bookmark"))
    implementation(project(":feature:photodetail"))
    implementation(libs.appcompat)
    implementation(platform(libs.composeBom))
    implementation(libs.hiltNavigationCompose)
    implementation(libs.composeActivity)
    implementation(libs.composeMaterial3)
    implementation(libs.accompanistInsets)
    implementation(libs.workManagerHilt)
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)
}
