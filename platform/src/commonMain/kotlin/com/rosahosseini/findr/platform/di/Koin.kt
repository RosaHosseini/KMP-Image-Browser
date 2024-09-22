package com.rosahosseini.findr.platform.di

import com.rosahosseini.findr.data.bookmark.di.bookmarkDataModule
import com.rosahosseini.findr.data.network.di.networkModule
import com.rosahosseini.findr.data.search.di.searchDataModule
import com.rosahosseini.findr.db.di.databaseFactoryModule
import com.rosahosseini.findr.db.di.localModule
import com.rosahosseini.findr.feature.bookmark.di.bookmarkFeatureModule
import com.rosahosseini.findr.feature.search.di.searchViewmodelModule
import com.rosahosseini.findr.feature.search.startups.startUpModule
import com.rosahosseini.findr.library.startup.di.startUpTasksModule
import java.com.rosahosseini.findr.library.di.coroutineModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            databaseFactoryModule,
            localModule,
            networkModule,
            bookmarkDataModule,
            searchDataModule,
            bookmarkFeatureModule,
            searchViewmodelModule,
            startUpModule,
            coroutineModule,
            startUpTasksModule
        )
    }
}
