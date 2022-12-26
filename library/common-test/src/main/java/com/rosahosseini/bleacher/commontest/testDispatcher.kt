package com.rosahosseini.bleacher.commontest

import com.rosahosseini.bleacher.core.AppDispatchers
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Delay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.coroutines.CoroutineContext

private val testScheduler = TestCoroutineScheduler()

@OptIn(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
class TestUiContext : CoroutineDispatcher(), Delay {
    override fun scheduleResumeAfterDelay(
        timeMillis: Long,
        continuation: CancellableContinuation<Unit>
    ) {
        continuation.resume(Unit, null)
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        block.run()  // dispatch on calling thread
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
val testDispatchers = AppDispatchers(
    main = UnconfinedTestDispatcher(testScheduler),
    io = UnconfinedTestDispatcher(testScheduler),
    default = UnconfinedTestDispatcher(testScheduler)
)