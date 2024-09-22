import com.android.build.gradle.BaseExtension
import com.rosahosseini.findr.buildlogic.androidLibs
import com.rosahosseini.findr.buildlogic.javaVersion
import com.rosahosseini.findr.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


internal class AndroidAppPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("androidApplication").get().get().pluginId)
        }
        extensions.configure<BaseExtension>(::configureKotlinAndroid)
    }
}

internal fun Project.configureKotlinAndroid(
    extension: BaseExtension
) = extension.apply {
    namespace = getModuleNameSpace()

    compileSdkVersion(androidLibs.findVersion("compileSdk").get().requiredVersion.toInt())
    defaultConfig {
        minSdk = androidLibs.findVersion("minSdk").get().requiredVersion.toInt()
        targetSdk = androidLibs.findVersion("targetSdk").get().requiredVersion.toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

@Suppress("UnstableApiUsage")
internal fun Project.getModuleNameSpace(): String {
    val moduleName = buildTreePath.split(":").joinToString(".")
    return (if (moduleName.isNotEmpty()) "$basePackageName$moduleName" else basePackageName)
}

private const val basePackageName = "com.rosahosseini.findr"
