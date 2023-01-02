package com.rosahosseini.findr.repository.impl

import com.rosahosseini.findr.local.datasource.BookmarkLocalDataSource
import com.rosahosseini.findr.model.Either
import com.rosahosseini.findr.model.ErrorModel
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.repository.BookmarkRepository
import com.rosahosseini.findr.repository.map.toPhoto
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BookmarkRepositoryImpl @Inject constructor(
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
        return bookmarkLocalDataSource.getBookmarkedPhotos().map { it.map { it.toPhoto() } }
    }
}