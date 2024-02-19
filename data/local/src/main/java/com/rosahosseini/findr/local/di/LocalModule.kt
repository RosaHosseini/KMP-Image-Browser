package com.rosahosseini.findr.local.di

import android.content.Context
import com.rosahosseini.findr.local.database.AppDatabase
import com.rosahosseini.findr.local.database.dao.PhotoDao
import com.rosahosseini.findr.local.database.dao.SearchDao
import com.rosahosseini.findr.local.database.dao.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.buildDatabase(appContext)
    }

    @Provides
    @Reusable
    fun provideSearchDao(database: AppDatabase): SearchDao {
        return database.searchDao()
    }

    @Provides
    @Reusable
    fun providePhotoDao(database: AppDatabase): PhotoDao {
        return database.photoDao()
    }

    @Provides
    @Reusable
    fun provideSearchHistoryDao(database: AppDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}
