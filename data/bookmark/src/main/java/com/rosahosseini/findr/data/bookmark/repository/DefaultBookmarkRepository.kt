package com.rosahosseini.findr.data.bookmark.repository

import com.rosahosseini.findr.data.bookmark.local.BookmarkLocalDataSource
import com.rosahosseini.findr.db.entity.PhotoEntity
import com.rosahosseini.findr.db.entity.toPhoto
import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import com.rosahosseini.findr.model.Either
import com.rosahosseini.findr.model.ErrorModel
import com.rosahosseini.findr.model.Photo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultBookmarkRepository @Inject constructor(
    private val bookmarkLocalDataSource: BookmarkLocalDataSource
) : BookmarkRepository {

    override suspend fun changeBookmarkState(photoId: String, isBookmarked: Boolean): Either<Unit> {
        return try {
            bookmarkLocalDataSource.changeBookmarkState(photoId, isBookmarked)
            Either.Success(Unit)
        } catch (ignored: Exception) {
            Either.Error(ErrorModel(ErrorModel.Type.DB))
        }
    }

    override fun getAllBookmarkedPhotos(): Flow<List<Photo>> {
        return bookmarkLocalDataSource.getBookmarkedPhotos().map { it.map(PhotoEntity::toPhoto) }
    }
}
