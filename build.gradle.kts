import com.android.build.gradle.BaseExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinJvm).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.hilt).apply(false)
    alias(libs.plugins.ktlint).apply(false)
    alias(libs.plugins.kotlinParcelize).apply(false)
    alias(libs.plugins.detekt)
}

allprojects {

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    configureKtlint()
    configureDetekt()
    configureJava()
    configureAndroidProjects()
}

fun Project.configureKtlint() {
    apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)
    configure<KtlintExtension> {
        version.set(rootProject.libs.versions.ktlint.get())
        android.set(true)
        filter {
            exclude { it.file.absolutePath.contains("/test/") }
        }
        outputToConsole.set(true)
        outputColorName.set("RED")
        enableExperimentalRules.set(true)
    }
}

fun Project.configureDetekt() {
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)
    configure<DetektExtension> {
        config.setFrom("$rootDir/detekt/detekt.yml")
        parallel = true
        autoCorrect = false
    }
    tasks.withType<Detekt>().configureEach {
        jvmTarget = "19"
        reports {
            html.required.set(false)
            xml.required.set(false)
            txt.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
        }
    }

    dependencies {
        detektPlugins(rootProject.libs.detektCompose)
    }
}

fun Project.configureAndroidProjects() {
    pluginManager.withPlugin(rootProject.libs.plugins.androidApplication.get().pluginId) {
        configureAndroidVersions()
    }
    pluginManager.withPlugin(rootProject.libs.plugins.androidLibrary.get().pluginId) {
        configureAndroidVersions()
    }
}

fun Project.configureAndroidVersions() {
    configure<BaseExtension> {
        compileSdkVersion(34)
        defaultConfig {
            minSdk = 23
            targetSdk = 34
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_19
            targetCompatibility = JavaVersion.VERSION_19
        }
    }
}

fun Project.configureJava() {
    plugins.withType<JavaBasePlugin>().configureEach {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion = JavaLanguageVersion.of("19")
            }
        }
    }
}
