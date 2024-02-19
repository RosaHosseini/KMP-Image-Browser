package com.rosahosseini.findr.feature.search.startups

import com.rosahosseini.findr.feature.search.worker.SearchWorkManagerScheduler
import com.rosahosseini.findr.startup.RunnableTask
import com.rosahosseini.findr.startup.StartupTaskKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object SearchStartUpModule {

    @Provides
    @IntoSet
    @StartupTaskKey
    fun provideCacheRecycler(
        searchWorkManagerScheduler: SearchWorkManagerScheduler
    ) = RunnableTask {
        searchWorkManagerScheduler.scheduleClearCachePeriodically()
    }
}
