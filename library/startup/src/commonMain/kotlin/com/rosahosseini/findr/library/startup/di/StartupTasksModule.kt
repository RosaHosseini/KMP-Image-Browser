package com.rosahosseini.findr.library.startup.di

import com.rosahosseini.findr.library.startup.StartupTask
import org.koin.dsl.module

val startUpTasksModule = module {
    factory<Set<StartupTask>> { getAll<StartupTask>().toSet() }
}
