import com.android.build.gradle.BaseExtension
import com.rosahosseini.findr.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

internal class NativeMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("kotlinCocoapods").get().get().pluginId)
            apply(libs.findPlugin("androidLibrary").get().get().pluginId)
        }

        extensions.configure<KotlinMultiplatformExtension>(::configureNativeMultiplatform)
        extensions.configure<BaseExtension>(::configureKotlinAndroid)
    }
}

internal fun Project.configureNativeMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {
    jvmToolchain(18)

    // targets
    androidTarget()
    listOf(
        iosArm64(),
        iosX64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {

            baseName = getIosBaseName()
            isStatic = true
        }
    }

    applyDefaultHierarchyTemplate()

    //applying the Cocoapods Configuration we made
    (this as ExtensionAware).extensions.configure<CocoapodsExtension>(::configureKotlinCocoapods)
}

@Suppress("UnstableApiUsage")
private fun Project.getIosBaseName(): String {
    return buildTreePath.split(":").drop(1).joinToString("_")
}