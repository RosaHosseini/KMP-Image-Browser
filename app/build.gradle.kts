import java.util.Properties

plugins {
    id("findr.android.application")
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

val versionProperties = loadIntoProjectProperties(filePath = "version.properties")

android {
    defaultConfig {
        applicationId = "com.rosahosseini.findr"
        versionCode = (versionProperties["appVersionCode"] as String?)?.toInt() ?: 1
        versionName = versionProperties["appVersionName"] as String? ?: "0.0.1"
    }

    buildFeatures {
        compose = true
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
    implementation(projects.platform)
    implementation(projects.library.startup)
    implementation(libs.appcompat)
    implementation(libs.composeActivity)
    implementation(platform(libs.koinBom))
    implementation(libs.koinAndroid)
    implementation(libs.koinWorkManager)
}

// Load keys into project properties
fun loadIntoProjectProperties(filePath: String): Properties {
    val propertiesFile: File = rootProject.file(filePath)
    val properties = Properties()
    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use(properties::load)
    }
    return properties
}
