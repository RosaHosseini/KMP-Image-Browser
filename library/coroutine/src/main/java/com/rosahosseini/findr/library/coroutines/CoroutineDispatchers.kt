
package com.rosahosseini.findr.library.coroutines

import kotlinx.coroutines.CoroutineDispatcher

data class CoroutineDispatchers(
    val main: CoroutineDispatcher,
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher
)
