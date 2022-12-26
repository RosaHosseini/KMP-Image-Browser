package com.rosahosseini.bleacher.repositoryimpl.di

import com.rosahosseini.bleacher.repository.BookmarkRepository
import com.rosahosseini.bleacher.repositoryimpl.BookmarkRepositoryImpl
import com.rosahosseini.bleacher.repository.SearchRepository
import com.rosahosseini.bleacher.repositoryimpl.SearchRepositoryImpl
import com.rosahosseini.bleacher.repositoryimpl.utils.ErrorManager
import com.rosahosseini.bleacher.repositoryimpl.utils.RemoteErrorManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    abstract fun bindBookmarkRepository(impl: BookmarkRepositoryImpl): BookmarkRepository

    @Binds
    abstract fun bindErrorManager(impl: RemoteErrorManager): ErrorManager
}