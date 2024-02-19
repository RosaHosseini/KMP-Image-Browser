package com.rosahosseini.findr.model

data class Photo(
    val id: String,
    val isBookmarked: Boolean,
    val title: String?,
    val description: String?,
    val url: String,
    val thumbnailUrl: String?
)
