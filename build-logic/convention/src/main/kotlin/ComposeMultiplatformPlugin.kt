import com.rosahosseini.findr.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension

internal class ComposeMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("composeCompiler").get().get().pluginId)
            apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
        }
        extensions.configure<ComposeExtension>(::configureResources)
    }
}

fun Project.configureResources(extension: ComposeExtension) {
    extension.configure<ResourcesExtension> {
        publicResClass = true
        packageOfResClass = getModuleNameSpace()
        generateResClass = auto
    }
}