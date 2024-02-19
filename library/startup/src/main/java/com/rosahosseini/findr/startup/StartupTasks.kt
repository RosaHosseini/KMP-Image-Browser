package com.rosahosseini.findr.startup

import javax.inject.Qualifier

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
 *     @IntoSet
 *     @StartupTaskKey
 *     fun provideStartupTask() = RunnableTask {
 *         Log.d("TAG", "This is a startup task!")
 *     }
 * }
 * ```
 *
 * ```
 * @BindType(contributesTo = BindType.Collection.SET)
 * @StartupTaskKey
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

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StartupTaskKey
