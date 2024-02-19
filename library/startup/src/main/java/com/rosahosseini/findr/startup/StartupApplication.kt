package com.rosahosseini.findr.startup

import android.app.Application
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

open class StartupApplication : Application() {

    @Inject
    @StartupTaskKey
    lateinit var startupTasks: @JvmSuppressWildcards Set<RunnableTask>

    override fun onCreate() {
        super.onCreate()
        runStartupTasks()
    }

    private fun runStartupTasks() {
        startupTasks.forEach { taskEntry ->
            MainScope().launch(Dispatchers.Default) {
                taskEntry.run()
            }
        }
        clearStartupTasks()
    }

    private fun clearStartupTasks() {
        startupTasks = emptySet()
    }
}
