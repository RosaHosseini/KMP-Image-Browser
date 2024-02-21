package com.rosahosseini.findr.data.bookmark.local

import com.rosahosseini.findr.db.dao.PhotoDao
import com.rosahosseini.findr.db.entity.PhotoEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class BookmarkLocalDataSource @Inject constructor(private val photoDao: PhotoDao) {
    suspend fun changeBookmarkState(photo: PhotoEntity) {
        photoDao.updateOrRemoveIfNotBookmark(photo)
    }

    fun getBookmarkedPhotos(): Flow<List<PhotoEntity>> {
        return photoDao.getBookmarkedFlow()
    }
}
