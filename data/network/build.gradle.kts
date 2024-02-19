plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.retrofit)
    implementation(platform(libs.okhttpBom))
    implementation(libs.okhttpLoggingInterceptor)
    implementation(libs.retrofitGsonConverter)
    implementation(libs.okhttp)
    implementation(libs.hiltCore)
    ksp(libs.hiltCompiler)
}
