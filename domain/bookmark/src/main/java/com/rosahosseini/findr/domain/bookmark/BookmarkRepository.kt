package com.rosahosseini.findr.domain.bookmark

import com.rosahosseini.findr.model.Photo
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun changeBookmarkState(photo: Photo, enabled: Boolean): Result<Unit>
    fun getAllBookmarkedPhotos(): Flow<List<Photo>>
}
