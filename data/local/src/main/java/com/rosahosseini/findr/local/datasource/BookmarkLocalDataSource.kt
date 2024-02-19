package com.rosahosseini.findr.local.datasource

import com.rosahosseini.findr.local.database.dao.PhotoDao
import com.rosahosseini.findr.local.database.entity.PhotoEntity
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
