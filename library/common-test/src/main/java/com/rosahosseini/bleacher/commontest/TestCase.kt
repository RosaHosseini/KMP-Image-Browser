package com.rosahosseini.bleacher.commontest

import kotlinx.coroutines.CoroutineScope

class TestCase {

    var given: (() -> Unit)? = null
    var whenever: (() -> Unit)? = null
    var then: (() -> Unit)? = null

    fun given(block: () -> Unit) {
        given = block
    }

    fun whenever(block: () -> Unit) {
        whenever = block
    }

    fun <T> whenever(input: T, block: T.() -> Unit) {
        whenever = {
            block(input)
        }
    }

    fun then(block: () -> Unit) {
        then = block
    }
}

class SuspendTestCase {

    var given: (suspend CoroutineScope.() -> Unit)? = null
    var whenever: (suspend CoroutineScope.() -> Unit)? = null
    var then: (suspend CoroutineScope.() -> Unit)? = null

    fun given(block: suspend CoroutineScope.() -> Unit) {
        given = block
    }

    fun whenever(block: suspend CoroutineScope.() -> Unit) {
        whenever = block
    }

    fun <T> whenever(input: T, block: suspend T.() -> Unit) {
        whenever = {
            block(input)
        }
    }

    fun then(block: suspend CoroutineScope.() -> Unit) {
        then = block
    }
}
