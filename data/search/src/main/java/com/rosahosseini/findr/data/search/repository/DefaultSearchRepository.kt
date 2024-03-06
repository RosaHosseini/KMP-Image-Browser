package com.rosahosseini.findr.data.search.repository

import com.rosahosseini.findr.data.search.local.SearchHistoryLocalDataSource
import com.rosahosseini.findr.data.search.remote.datasource.PhotoRemoteDataSource
import com.rosahosseini.findr.data.search.remote.response.SearchPhotosDto
import com.rosahosseini.findr.data.search.remote.response.toPagePhotos
import com.rosahosseini.findr.db.entity.SearchHistoryEntity
import com.rosahosseini.findr.domain.search.SearchRepository
import com.rosahosseini.findr.library.coroutines.CoroutineDispatchers
import com.rosahosseini.findr.model.Page
import com.rosahosseini.findr.model.Photo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultSearchRepository @Inject constructor(
    private val photosRemoteDataSource: PhotoRemoteDataSource,
    private val searchHistoryLocalDataSource: SearchHistoryLocalDataSource,
    private val coroutineDispatchers: CoroutineDispatchers
) : SearchRepository {

    override suspend fun searchPhotos(
        query: String,
        pageNumber: Int,
        pageSize: Int
    ): Result<Page<Photo>> {
        return withContext(coroutineDispatchers.default) {
            photosRemoteDataSource.search(query, pageSize, pageNumber)
                .map(SearchPhotosDto::toPagePhotos)
        }
    }

    override suspend fun getRecentPhotos(
        pageNumber: Int,
        pageSize: Int
    ): Result<Page<Photo>> {
        return withContext(coroutineDispatchers.default) {
            photosRemoteDataSource.getRecent(pageSize, pageNumber)
                .map(SearchPhotosDto::toPagePhotos)
        }
    }

    override suspend fun clearExpiredData(expiredTimeMillis: Long) {
        searchHistoryLocalDataSource.clearExpiredQueries(expiredTimeMillis)
    }

    override fun getSearchSuggestion(query: String, limit: Int): Flow<List<String>> {
        return searchHistoryLocalDataSource.getLatestQueries(query, limit)
            .map { it.map(SearchHistoryEntity::queryText) }
            .flowOn(coroutineDispatchers.default)
    }

    override suspend fun removeSearchQuery(query: String) {
        searchHistoryLocalDataSource.removeQuery(query)
    }

    override suspend fun saveSearchQuery(query: String) {
        searchHistoryLocalDataSource.saveQuery(query)
    }
}
