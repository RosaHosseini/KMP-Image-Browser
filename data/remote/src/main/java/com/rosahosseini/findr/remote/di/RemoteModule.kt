package com.rosahosseini.findr.remote.di

import com.rosahosseini.findr.remote.retrofit.FLICKR_BASE_URL
import com.rosahosseini.findr.remote.retrofit.buildRetrofit
import com.rosahosseini.findr.remote.service.PhotoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
internal object RemoteModule {
    // RETROFIT
    @Provides
    fun provideOKHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    fun provideFlickrRetrofit(client: OkHttpClient): Retrofit {
        return buildRetrofit(FLICKR_BASE_URL, client)
    }

    // SERVICES
    @Provides
    fun provideFlickrService(retrofit: Retrofit): PhotoService {
        return retrofit.create(PhotoService::class.java)
    }
}
