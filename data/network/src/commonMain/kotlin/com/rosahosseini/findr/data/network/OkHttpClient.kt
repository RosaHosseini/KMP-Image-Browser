package com.rosahosseini.findr.data.network

import com.rosahosseini.findr.data.common.remote.ErrorManager
import com.rosahosseini.findr.domain.model.BuildConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun configureOkHttpClient(
    engine: HttpClientEngine,
    buildConfiguration: BuildConfiguration,
    errorManager: ErrorManager
): HttpClient {
    return HttpClient(engine) {
        expectSuccess = true
        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, _ ->
                throw errorManager.apiError(cause)
            }
        }
        install(Logging) {
            level = if (buildConfiguration.isDebug) LogLevel.ALL else LogLevel.NONE
            logger = configureClientLogger()
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = false
                    explicitNulls = false
                }
            )
        }
    }
}

internal expect fun configureHttpClientEngine(): HttpClientEngine

internal expect fun configureClientLogger(): Logger
