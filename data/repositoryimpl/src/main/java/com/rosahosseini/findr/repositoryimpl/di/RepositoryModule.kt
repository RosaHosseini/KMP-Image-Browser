package com.rosahosseini.findr.repositoryimpl.di

import com.rosahosseini.findr.repository.BookmarkRepository
import com.rosahosseini.findr.repositoryimpl.BookmarkRepositoryImpl
import com.rosahosseini.findr.repository.SearchRepository
import com.rosahosseini.findr.repositoryimpl.SearchRepositoryImpl
import com.rosahosseini.findr.repositoryimpl.utils.ErrorManager
import com.rosahosseini.findr.repositoryimpl.utils.RemoteErrorManager
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