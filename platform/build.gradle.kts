import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

plugins {
    id("findr.kotlin.multiplatform.native")
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    id("com.codingfeline.buildkonfig") version "0.15.2"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.library.imageloader)
            implementation(projects.library.ui)
            implementation(projects.library.startup)
            implementation(projects.library.coroutine)
            implementation(projects.data.network)
            implementation(projects.data.db)
            implementation(projects.data.search)
            implementation(projects.data.bookmark)
            implementation(projects.domain.model)
            implementation(projects.feature.search)
            implementation(projects.feature.bookmark)
            implementation(projects.feature.photodetail)

            implementation(compose.foundation)
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)
            implementation(libs.immutableCollections)
            implementation(libs.composeNavigation)
            implementation(libs.lifecycleCompose)
            implementation(project.dependencies.platform(libs.koinBom))
            implementation(libs.koinComposeViewmodel)
            implementation(libs.coilCompose)
        }
    }
    task("testClasses")
}

android {
    buildFeatures {
        buildConfig = true
    }
}

val apiKeyProperties = loadIntoProjectProperties(filePath = "apikey.properties")

buildkonfig {
    packageName = "com.rosahosseini.findr"

    val flickrKey = apiKeyProperties["FLICKR_KEY"] as? String? ?: ""
    println("FLICKR_KEY: $flickrKey")
    defaultConfigs {
        buildConfigField(
            STRING,
            "FLICKR_API_KEY",
            flickrKey
        )
    }
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
