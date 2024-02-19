package com.rosahosseini.findr.data.bookmark.di

import com.rosahosseini.findr.data.bookmark.repository.DefaultBookmarkRepository
import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
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
    fun bindBookmarkRepository(impl: DefaultBookmarkRepository): BookmarkRepository
}
