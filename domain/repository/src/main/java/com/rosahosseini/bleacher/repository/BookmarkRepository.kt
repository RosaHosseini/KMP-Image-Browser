package com.rosahosseini.bleacher.repository

import com.rosahosseini.bleacher.model.Either
import com.rosahosseini.bleacher.model.Photo
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun changeBookmarkState(photoId: String, isBookmarked: Boolean): Either<Unit>
    fun getAllBookmarkedPhotos(): Flow<List<Photo>>
}