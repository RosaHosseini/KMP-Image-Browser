package com.rosahosseini.bleacher.local.datasource

import com.rosahosseini.bleacher.local.database.dao.PhotoDao
import com.rosahosseini.bleacher.local.database.entity.PhotoEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class BookmarkLocalDataSource @Inject constructor(private val photoDao: PhotoDao) {
    suspend fun changeBookmarkState(photoId: String, isBookmarked: Boolean) {
        photoDao.updateBookmark(photoId, isBookmarked)
    }

    fun getBookmarkedPhotos(): Flow<List<PhotoEntity>> {
        return photoDao.getBookmarkedFlow()
    }
}