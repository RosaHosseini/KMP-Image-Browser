package com.rosahosseini.findr.domain.model

data class Page<T>(
    val items: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val hasNext: Boolean = true
)
