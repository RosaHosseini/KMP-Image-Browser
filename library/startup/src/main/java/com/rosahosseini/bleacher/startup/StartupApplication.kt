package com.rosahosseini.bleacher.startup

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

open class StartupApplication : Application() {

    @Inject
    @JvmField
    var startupTasks: StartupTasks? = null

    override fun onCreate() {
        super.onCreate()
        runStartupTasks()
    }

    private fun runStartupTasks() {
        requireNotNull(startupTasks).entries.sortedBy { it.key.order }.forEach { taskEntry ->
            MainScope().launch(Dispatchers.Default) {
                taskEntry.value.run()
            }
        }
        clearStartupTasks()
    }

    private fun clearStartupTasks() {
        startupTasks = null
    }
}