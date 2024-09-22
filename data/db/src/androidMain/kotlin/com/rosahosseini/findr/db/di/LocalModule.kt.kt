package com.rosahosseini.findr.db.di

import androidx.room.RoomDatabase
import com.rosahosseini.findr.db.AppDatabase
import com.rosahosseini.findr.db.getAppDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val databaseFactoryModule: Module = module {
    single<RoomDatabase.Builder<AppDatabase>> { getAppDatabaseBuilder(androidContext()) }
}
