package com.rosahosseini.findr.data.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger

internal actual fun configureHttpClientEngine(): HttpClientEngine {
    return OkHttp.create()
}

internal actual fun configureClientLogger(): Logger {
    return Logger.ANDROID
}
