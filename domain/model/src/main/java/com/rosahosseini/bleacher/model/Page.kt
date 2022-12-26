package com.rosahosseini.bleacher.model

data class Page<T>(
    val items: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val hasNext: Boolean = true,
    val timeStamp: Long
)