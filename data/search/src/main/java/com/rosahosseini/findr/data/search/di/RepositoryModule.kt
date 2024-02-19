package com.rosahosseini.findr.data.search.di

import com.rosahosseini.findr.data.search.repository.DefaultSearchRepository
import com.rosahosseini.findr.domain.search.SearchRepository
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
    fun bindSearchRepository(repo: DefaultSearchRepository): SearchRepository
}
