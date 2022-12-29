package com.rosahosseini.findr.startup

import com.google.auto.value.AutoAnnotation
import dagger.MapKey

/**
 * The RunnableTask should be implemented by any class whose wants to execute codes for initializing
 * purposes.
 * The corresponding codes must be placed in any thread except Main.
 *
 * Examples:
 *
 * ```
 * @Module
 * @InstallIn(SingletonComponent::class)
 * object StartupTasksModule {
 *
 *     @Provides
 *     @IntoMap
 *     @StartupTaskKey("MyStartupTask")
 *     fun provideStartupTask() = RunnableTask {
 *         Log.d("TAG", "This is a startup task!")
 *     }
 * }
 * ```
 *
 * ```
 * @BindType(contributesTo = BindType.Collection.MAP)
 * @StartupTaskKey("MyStartupTask")
 * class MyStartupTask : RunnableTask {
 *
 *     override suspend fun run() {
 *         Log.d("TAG", "This is a startup task!")
 *     }
 * }
 * ```
 */
fun interface RunnableTask {

    /**
     * This function will execute in [kotlinx.coroutines.Dispatchers.Default]
     */
    suspend fun run()
}

typealias StartupTasks = MutableMap<StartupTaskKey, RunnableTask>

@MapKey(unwrapValue = false)
annotation class StartupTaskKey(val name: String, val order: Int = Int.MAX_VALUE)

@AutoAnnotation
fun createStartupTaskKey(name: String, order: Int): StartupTaskKey {
    return StartupTaskKeyCreator.createStartupTaskKey(name, order)
}