package com.rosahosseini.bleacher.repositoryimpl

import com.rosahosseini.bleacher.local.datasource.BookmarkLocalDataSource
import com.rosahosseini.bleacher.model.Either
import com.rosahosseini.bleacher.model.ErrorModel
import com.rosahosseini.bleacher.model.Photo
import com.rosahosseini.bleacher.repository.BookmarkRepository
import com.rosahosseini.bleacher.repositoryimpl.map.toPhoto
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepositoryImpl @Inject constructor(
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