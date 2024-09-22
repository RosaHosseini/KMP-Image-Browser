package com.rosahosseini.findr.feature.search.startups

import com.rosahosseini.findr.feature.search.worker.SearchWorkManagerScheduler
import com.rosahosseini.findr.library.startup.StartupTask
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val startUpModule = module {

    factoryOf(::SearchWorkManagerScheduler)
    factory {
        StartupTask {
            get<SearchWorkManagerScheduler>().scheduleClearCachePeriodically()
        }
    }
}
