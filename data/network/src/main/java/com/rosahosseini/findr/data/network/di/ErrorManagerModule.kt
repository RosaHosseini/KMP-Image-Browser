package com.rosahosseini.findr.data.network.di

import com.rosahosseini.findr.ErrorManager
import com.rosahosseini.findr.data.network.RemoteErrorManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface ErrorManagerModule {

    @Singleton
    @Binds
    fun bindsErrorManager(manager: RemoteErrorManager): ErrorManager
}
