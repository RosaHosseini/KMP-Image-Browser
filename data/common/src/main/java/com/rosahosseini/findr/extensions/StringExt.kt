package com.rosahosseini.findr.extensions

fun String.orNullIfEmpty(): String? {
    return if (isNullOrEmpty()) null else this
}
