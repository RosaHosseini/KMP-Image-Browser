package com.rosahosseini.findr.library.imageloader

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun getNetworkClientEngine(): HttpClientEngine {
    return OkHttp.create()
}
