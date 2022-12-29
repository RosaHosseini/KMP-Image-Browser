package com.rosahosseini.findr.search.startups

import com.rosahosseini.findr.search.worker.SearchWorkManagerScheduler
import com.rosahosseini.findr.startup.StartupTaskKey
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