package com.rosahosseini.findr.data.search.local

import com.rosahosseini.findr.db.dao.PhotoDao
import com.rosahosseini.findr.db.dao.SearchDao
import com.rosahosseini.findr.db.entity.SearchedPhotoEntity
import com.rosahosseini.findr.extensions.getCurrentTimeMillis
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SearchPhotoLocalDataSource @Inject constructor(
    private val searchDao: SearchDao,
    private val photoDao: PhotoDao
) {
    suspend fun saveSearch(data: List<SearchedPhotoEntity>) {
        val photos = data.map {
            val isBookmarked = photoDao.getPhoto(it.photoEntity.photoId)?.isBookmarked ?: false
            it.photoEntity.copy(isBookmarked = isBookmarked)
        }
        photoDao.insertOrUpdate(photos)
        searchDao.insertOrUpdate(data.map { it.searchEntity })
    }

    suspend fun clearExpiredQueries(expireTimeMillis: Long) {
        check(expireTimeMillis > 0) { "expire time should be positive" }
        val leastTimestamp = getCurrentTimeMillis() - expireTimeMillis
        searchDao.clearExpiredSearch(leastTimestamp)
        photoDao.clearExpiredBookmarkedPhotos(leastTimestamp)
    }

    suspend fun search(query: String?, page: Int, limit: Int): List<SearchedPhotoEntity> {
        return searchDao.search(
            queryText = query.orEmpty(),
            from = page * limit,
            to = (page + 1) * limit - 1
        )
    }

    fun searchFlow(
        query: String?,
        fromPage: Int,
        toPage: Int,
        limit: Int
    ): Flow<List<SearchedPhotoEntity>> {
        return searchDao.searchFlow(
            queryText = query.orEmpty(),
            from = fromPage * limit,
            to = (toPage + 1) * limit - 1
        ).map { it }
    }
}
