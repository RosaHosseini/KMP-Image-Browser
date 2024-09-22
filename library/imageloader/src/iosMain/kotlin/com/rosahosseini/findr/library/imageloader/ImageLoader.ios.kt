package com.rosahosseini.findr.library.imageloader

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

internal actual fun getNetworkClientEngine(): HttpClientEngine {
    return Darwin.create()
}
