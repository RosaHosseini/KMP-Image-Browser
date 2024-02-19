package com.rosahosseini.findr.data.network.di

import com.rosahosseini.findr.data.network.FLICKR_BASE_URL
import com.rosahosseini.findr.data.network.buildRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
internal object RetrofitModule {
    // RETROFIT
    @Singleton
    @Provides
    fun provideOKHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    fun provideFlickrRetrofit(client: OkHttpClient): Retrofit {
        return buildRetrofit(FLICKR_BASE_URL, client)
    }
}
