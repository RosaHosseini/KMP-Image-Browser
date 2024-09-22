
import org.jlleitschuh.gradle.ktlint.KtlintExtension

val javaVersion = JavaVersion.VERSION_18

plugins {
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.composeCompiler).apply(false)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.ktlint).apply(false)
    alias(libs.plugins.kotlinParcelize).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
    alias(libs.plugins.room).apply(false)
}

allprojects {

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    configureKtlint()
//    configureDetekt()
    configureJava()
}

fun Project.configureKtlint() {
    apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)
    configure<KtlintExtension> {
        version.set(rootProject.libs.versions.ktlint.get())
        android.set(true)
        filter {
            exclude { it.file.absolutePath.contains("/build/") }
            exclude { it.file.absolutePath.contains("/test/") }
            exclude { it.file.absolutePath.contains("/commonTest/") }
            exclude { it.file.absolutePath.contains("/androidTest/") }
            exclude { it.file.absolutePath.contains("/androidUnitTest/") }
            exclude { it.file.absolutePath.contains("/androidInstrumentedTest/") }
            exclude { it.file.absolutePath.contains("/iosTest/") }
        }
        outputToConsole.set(true)
        outputColorName.set("RED")
        enableExperimentalRules.set(true)
    }
}

// fun Project.configureDetekt() {
//    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)
//    configure<DetektExtension> {
//        config.setFrom("$rootDir/detekt/detekt.yml")
//        parallel = true
//        autoCorrect = false
//    }
//    tasks.withType<Detekt>().configureEach {
//        jvmTarget = javaVersion.majorVersion
//        reports {
//            html.required.set(false)
//            xml.required.set(false)
//            txt.required.set(false)
//            sarif.required.set(false)
//            md.required.set(false)
//        }
//    }
//
//    dependencies {
//        detektPlugins(rootProject.libs.detektCompose)
//    }
// }

fun Project.configureJava() {
    plugins.withType<JavaBasePlugin>().configureEach {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion = JavaLanguageVersion.of(javaVersion.majorVersion)
            }
        }
    }
}
