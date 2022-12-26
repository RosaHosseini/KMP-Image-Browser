package com.rosahosseini.bleacher.core.extensions

fun String.orNullIfEmpty(): String? {
    return if (isNullOrEmpty()) null else this
}