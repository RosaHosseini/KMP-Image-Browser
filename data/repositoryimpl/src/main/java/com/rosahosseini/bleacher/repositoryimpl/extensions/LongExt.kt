package com.rosahosseini.bleacher.repositoryimpl.extensions

fun Long.isInDurationOf(milliSeconds: Long): Boolean {
    val currentTime = System.currentTimeMillis()
    check(this <= currentTime) { "the timestamp is after now!" }
    return (currentTime - this) <= milliSeconds
}