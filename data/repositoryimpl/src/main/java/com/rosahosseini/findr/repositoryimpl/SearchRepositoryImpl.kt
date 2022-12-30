package com.rosahosseini.findr.repositoryimpl

import com.rosahosseini.findr.core.extensions.isInDurationOf
import com.rosahosseini.findr.local.datasource.SearchHistoryLocalDataSource
import com.rosahosseini.findr.local.datasource.SearchPhotoLocalDataSource
import com.rosahosseini.findr.model.Either
import com.rosahosseini.findr.model.Page
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.remote.datasource.PhotoRemoteDataSource
import com.rosahosseini.findr.repository.SearchRepository
import com.rosahosseini.findr.repositoryimpl.map.toPagePhotos
import com.rosahosseini.findr.repositoryimpl.map.toPhoto
import com.rosahosseini.findr.repositoryimpl.map.toSearchedPhotosEntities
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.rosahosseini.findr.repositoryimpl.utils.RequestManager.Companion.Builder as RequestManagerBuilder

class SearchRepositoryImpl @Inject constructor(
    private val photosRemoteDataSource: PhotoRemoteDataSource,
    private val searchLocalDataSource: SearchPhotoLocalDataSource,
    private val searchHistoryLocalDataSource: SearchHistoryLocalDataSource
) : SearchRepository {

    override suspend fun searchPhotos(
        query: String,
        pageNumber: Int,
        limit: Int
    ): Flow<Either<Page<Photo>>> = RequestManagerBuilder<Page<Photo>>()
        .networkCall { photosRemoteDataSource.search(query, limit, pageNumber).toPagePhotos() }
        .loadFromDb { searchLocalDataSource.search(query, pageNumber, limit).toPagePhotos(limit) }
        .saveCallResults { searchLocalDataSource.saveSearch(it.toSearchedPhotosEntities(query)) }
        .shouldFetch { it?.isDataOutDated() ?: true }
        .request()

    override suspend fun getRecentPhotos(
        pageNumber: Int,
        limit: Int
    ): Flow<Either<Page<Photo>>> = RequestManagerBuilder<Page<Photo>>()
        .networkCall { photosRemoteDataSource.getRecent(limit, pageNumber).toPagePhotos() }
        .loadFromDb {
            searchLocalDataSource.search(query = null, pageNumber, limit).toPagePhotos(limit)
        }
        .saveCallResults {
            searchLocalDataSource.saveSearch(it.toSearchedPhotosEntities(query = null))
        }
        .shouldFetch { it?.isDataOutDated() ?: true }
        .request()

    override fun searchLocalPhotos(
        query: String?,
        fromPage: Int,
        toPage: Int,
        limit: Int
    ): Flow<List<Photo>> {
        return searchLocalDataSource.searchFlow(query, fromPage, toPage, limit)
            .map { it.map { it.photoEntity.toPhoto() } }
    }

    override suspend fun clearExpiredData(expiredTimeMillis: Long) {
        searchLocalDataSource.clearExpiredQueries(expiredTimeMillis)
        searchHistoryLocalDataSource.clearExpiredQueries(expiredTimeMillis)
    }

    override fun getSearchSuggestion(query: String, limit: Int): Flow<List<String>> {
        return searchHistoryLocalDataSource.getLatestQueries(query, limit)
            .map { it.map { it.queryText } }
    }

    override suspend fun removeSuggestion(query: String) {
        searchHistoryLocalDataSource.removeQuery(query)
    }

    override suspend fun saveSearchQuery(query: String) {
        searchHistoryLocalDataSource.saveQuery(query)
    }

    private fun Page<Photo>.isDataOutDated(): Boolean {
        return timeStamp.isInDurationOf(DB_OUTDATED_MILLI_SECONDS).not()
    }

    companion object {
        private const val DB_OUTDATED_MILLI_SECONDS = 1000 * 60 * 30L
    }
}