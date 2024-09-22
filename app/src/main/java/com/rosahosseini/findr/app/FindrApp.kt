package com.rosahosseini.findr.app

import com.rosahosseini.findr.library.startup.StartupApplication
import com.rosahosseini.findr.platform.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent

class FindrApp : StartupApplication(), KoinComponent {

    override fun onCreate() {
        initKoin {
            androidContext(this@FindrApp)
            workManagerFactory()
        }
        super.onCreate()
    }
}
