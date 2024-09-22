package com.rosahosseini.findr.db.di

import com.rosahosseini.findr.db.AppDatabase
import com.rosahosseini.findr.db.createAppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val databaseFactoryModule: Module

val localModule = module {
    single<AppDatabase> { createAppDatabase(get(), get()) }

    single { get<AppDatabase>().photoDao() }
    single { get<AppDatabase>().searchHistoryDao() }
}
