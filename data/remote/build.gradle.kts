import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.retrofitGsonConverter)
    implementation(libs.okhttp)
    implementation(libs.hiltCore)
    ksp(libs.hiltCompiler)
}
