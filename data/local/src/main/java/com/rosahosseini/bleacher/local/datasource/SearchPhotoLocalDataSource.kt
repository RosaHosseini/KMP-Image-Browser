package com.rosahosseini.bleacher.local.datasource

import com.rosahosseini.bleacher.local.database.dao.PhotoDao
import com.rosahosseini.bleacher.local.database.dao.SearchDao
import com.rosahosseini.bleacher.local.database.entity.SearchedPhotoEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.rosahosseini.bleacher.core.extensions.getCurrentTimeMillis

class SearchPhotoLocalDataSource @Inject constructor(
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