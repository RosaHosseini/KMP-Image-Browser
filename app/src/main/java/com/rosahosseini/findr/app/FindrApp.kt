package com.rosahosseini.findr.app

import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.rosahosseini.findr.startup.StartupApplication
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FindrApp : StartupApplication(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
