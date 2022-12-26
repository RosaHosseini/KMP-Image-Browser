package com.rosahosseini.bleacher.local.di

import android.content.Context
import com.rosahosseini.bleacher.local.database.AppDatabase
import com.rosahosseini.bleacher.local.database.dao.PhotoDao
import com.rosahosseini.bleacher.local.database.dao.SearchDao
import dagger.Module
import dagger.Provides
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
    fun provideSearchDao(database: AppDatabase): SearchDao {
        return database.searchDao()
    }

    @Provides
    fun providePhotoDao(database: AppDatabase): PhotoDao {
        return database.photoDao()
    }
}