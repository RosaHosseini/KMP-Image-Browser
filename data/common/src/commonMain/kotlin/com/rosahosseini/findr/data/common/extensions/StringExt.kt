package com.rosahosseini.findr.data.common.extensions

fun String.orNullIfEmpty(): String? {
    return if (isNullOrEmpty()) null else this
}
