package com.rosahosseini.findr.db.di

import android.content.Context
import com.rosahosseini.findr.db.AppDatabase
import com.rosahosseini.findr.db.dao.PhotoDao
import com.rosahosseini.findr.db.dao.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class LocalModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.buildDatabase(appContext)
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
