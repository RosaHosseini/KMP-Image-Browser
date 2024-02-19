
package com.rosahosseini.findr.core

import kotlinx.coroutines.CoroutineDispatcher

data class AppDispatchers(
    val main: CoroutineDispatcher,
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher
)
