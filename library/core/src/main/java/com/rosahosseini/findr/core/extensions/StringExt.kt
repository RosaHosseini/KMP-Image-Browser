package com.rosahosseini.findr.core.extensions

fun String.orNullIfEmpty(): String? {
    return if (isNullOrEmpty()) null else this
}
