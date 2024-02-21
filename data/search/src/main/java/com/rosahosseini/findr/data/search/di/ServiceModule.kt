package com.rosahosseini.findr.data.search.di

import com.rosahosseini.findr.data.search.remote.service.SearchService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
internal object ServiceModule {

    @Provides
    @Reusable
    fun providePhotoService(retrofit: Retrofit): SearchService {
        return retrofit.create(SearchService::class.java)
    }
}
