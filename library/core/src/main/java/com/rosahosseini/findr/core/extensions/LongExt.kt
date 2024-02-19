package com.rosahosseini.findr.core.extensions

fun Long.isInDurationOf(milliSeconds: Long): Boolean {
    val currentTime = getCurrentTimeMillis()
    check(this <= currentTime) { "the timestamp is after now!" }
    return (currentTime - this) <= milliSeconds
}

fun getCurrentTimeMillis() = System.currentTimeMillis()
