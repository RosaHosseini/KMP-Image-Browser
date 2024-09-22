package com.rosahosseini.findr.data.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE

internal actual fun configureHttpClientEngine(): HttpClientEngine {
    return Darwin.create()
}

internal actual fun configureClientLogger(): Logger {
    return Logger.SIMPLE
}
