package com.rosahosseini.findr.domain.model

data class Photo(
    val id: String,
    val title: String?,
    val description: String?,
    val url: String,
    val thumbnailUrl: String?
)
