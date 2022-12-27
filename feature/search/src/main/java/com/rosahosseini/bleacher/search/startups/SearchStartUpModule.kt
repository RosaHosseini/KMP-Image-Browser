package com.rosahosseini.bleacher.search.startups

import com.rosahosseini.bleacher.search.worker.SearchWorkManagerScheduler
import com.rosahosseini.bleacher.startup.StartupTaskKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
object SearchStartUpModule {

    @Provides
    @IntoMap
    @StartupTaskKey("cache-recycler")
    fun provideCacheRecycler(searchWorkManagerScheduler: SearchWorkManagerScheduler) = Runnable {
        searchWorkManagerScheduler.scheduleClearCachePeriodically()
    }
}