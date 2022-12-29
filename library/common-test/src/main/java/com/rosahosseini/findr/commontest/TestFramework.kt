package com.rosahosseini.findr.commontest

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.EmptyCoroutineContext

inline fun testCase(testCase: TestCase.() -> Unit) {
    TestCase().apply(testCase).apply {
        given?.invoke()
        whenever?.invoke() ?: throw NotImplementedError("You need to implement whenever")
        then?.invoke() ?: throw NotImplementedError("You need to implement then")
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
inline fun coroutineTestCase(
    crossinline testCase: SuspendTestCase.() -> Unit
) {
    runTest(EmptyCoroutineContext) {
        SuspendTestCase().apply(testCase).apply {
            given?.invoke(this@runTest)
            whenever?.invoke(this@runTest)
            then?.invoke(this@runTest)
                ?: throw NotImplementedError("You need to implement then ")
        }
    }
}