package com.rosahosseini.bleacher.local.datasource

import com.rosahosseini.bleacher.local.database.dao.PhotoDao
import com.rosahosseini.bleacher.local.database.dao.SearchDao
import com.rosahosseini.bleacher.local.database.entity.SearchedPhotoEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    suspend fun search(query: String?, page: Int, limit: Int): List<SearchedPhotoEntity> {
        return searchDao.search(
            queryText = query.orEmpty(),
            from = (page - 1) * limit,
            to = (page * limit) - 1
        )
    }

    fun searchFlow(query: String?): Flow<List<SearchedPhotoEntity>> {
        return searchDao.searchFlow(query.orEmpty()).map { it }
    }
}