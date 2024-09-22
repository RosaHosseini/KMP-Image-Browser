package com.rosahosseini.findr.data.common.extensions

fun Long.isInDurationOf(deltaMilliSeconds: Long, currentTime: Long): Boolean {
    check(this <= currentTime) { "the timestamp is after now!" }
    return (currentTime - this) <= deltaMilliSeconds
}
