package com.rosahosseini.findr.domain.bookmark

import com.rosahosseini.findr.model.Either
import com.rosahosseini.findr.model.Photo
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun changeBookmarkState(photoId: String, isBookmarked: Boolean): Either<Unit>
    fun getAllBookmarkedPhotos(): Flow<List<Photo>>
}
