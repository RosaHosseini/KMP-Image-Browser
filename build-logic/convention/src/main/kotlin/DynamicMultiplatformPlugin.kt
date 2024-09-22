import com.rosahosseini.findr.buildlogic.javaVersion
import com.rosahosseini.findr.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

internal class DynamicMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("kotlinCocoapods").get().get().pluginId)
        }

        extensions.configure<KotlinMultiplatformExtension>(::configureDynamicMultiplatform)
    }
}

internal fun Project.configureDynamicMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {
    jvmToolchain(javaVersion.majorVersion.toInt())

    // targets
    jvm()
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()
    //applying the Cocoapods Configuration we made
    (this as ExtensionAware).extensions.configure<CocoapodsExtension>(::configureKotlinCocoapods)
}