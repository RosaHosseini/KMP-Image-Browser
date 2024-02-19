package com.rosahosseini.findr.repository.di

import com.rosahosseini.findr.repository.BookmarkRepository
import com.rosahosseini.findr.repository.SearchRepository
import com.rosahosseini.findr.repository.impl.BookmarkRepositoryImpl
import com.rosahosseini.findr.repository.impl.SearchRepositoryImpl
import com.rosahosseini.findr.repository.utils.ErrorManager
import com.rosahosseini.findr.repository.utils.RemoteErrorManager
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    @Reusable
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    @Reusable
    fun bindBookmarkRepository(impl: BookmarkRepositoryImpl): BookmarkRepository

    @Binds
    @Reusable
    fun bindErrorManager(impl: RemoteErrorManager): ErrorManager
}
