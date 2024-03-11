package com.rosahosseini.findr.data.network.di

import com.google.gson.Gson
import com.rosahosseini.findr.data.network.FLICKR_BASE_URL
import com.rosahosseini.findr.data.network.KotlinResultCallAdapterFactory
import com.rosahosseini.findr.data.network.RemoteErrorManager
import com.rosahosseini.findr.model.BuildConfiguration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
internal object RetrofitModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Singleton
    @IntoSet
    @Provides
    fun provideLoggingInterceptor(buildConfiguration: BuildConfiguration): Interceptor {
        val level = if (buildConfiguration.isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return HttpLoggingInterceptor().setLevel(level)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient {
        return OkHttpClient.Builder().apply {
            interceptors.forEach { interceptor -> addInterceptor(interceptor) }
        }.build()
    }

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = FLICKR_BASE_URL

    @Singleton
    @Provides
    @IntoSet
    fun providesGsonConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    @IntoSet
    fun providesKotlinResultCallAdapterFactory(
        errorManager: RemoteErrorManager
    ): CallAdapter.Factory {
        return KotlinResultCallAdapterFactory(errorManager)
    }

    @Provides
    @Singleton
    fun provideFlickrRetrofit(
        @BaseUrl baseUrl: String,
        client: OkHttpClient,
        converterFactories: Set<@JvmSuppressWildcards Converter.Factory>,
        callAdapterFactories: Set<@JvmSuppressWildcards CallAdapter.Factory>
    ): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(baseUrl)
            converterFactories.forEach(::addConverterFactory)
            callAdapterFactories.forEach(::addCallAdapterFactory)
            client(client)
        }.build()
    }
}
