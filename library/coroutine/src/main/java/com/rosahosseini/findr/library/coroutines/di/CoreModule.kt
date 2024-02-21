package com.rosahosseini.findr.library.coroutines.di

import com.rosahosseini.findr.library.coroutines.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
class CoreModule {
    @Provides
    fun provideAppDispatcher() = CoroutineDispatchers(
        Dispatchers.Main,
        Dispatchers.Default,
        Dispatchers.IO
    )
}
