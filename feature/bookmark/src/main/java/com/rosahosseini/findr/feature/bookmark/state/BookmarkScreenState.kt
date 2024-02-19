package com.rosahosseini.findr.feature.bookmark.state

import com.rosahosseini.findr.model.Photo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class BookmarkScreenState(
    val photos: ImmutableList<Photo> = persistentListOf()
)
