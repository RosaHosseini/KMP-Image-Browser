package com.rosahosseini.findr.data.network.di

import com.rosahosseini.findr.data.common.di.flickerUrlQualifier
import com.rosahosseini.findr.data.common.remote.ErrorManager
import com.rosahosseini.findr.data.network.RemoteErrorManager
import com.rosahosseini.findr.data.network.configureHttpClientEngine
import com.rosahosseini.findr.data.network.configureOkHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val FLICKR_BASE_URL = "https://api.flickr.com/services/rest/"

val networkModule = module {
    factoryOf(::RemoteErrorManager) bind ErrorManager::class

    single<HttpClientEngine> { configureHttpClientEngine() }

    single<HttpClient> { configureOkHttpClient(get(), get(), get()) }

    factory(flickerUrlQualifier) { FLICKR_BASE_URL }
}
