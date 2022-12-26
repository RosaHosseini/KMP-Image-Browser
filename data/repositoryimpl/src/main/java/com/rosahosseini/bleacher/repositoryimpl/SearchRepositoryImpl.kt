package com.rosahosseini.bleacher.repositoryimpl

import com.rosahosseini.bleacher.local.datasource.SearchPhotoLocalDataSource
import com.rosahosseini.bleacher.model.Either
import com.rosahosseini.bleacher.model.Page
import com.rosahosseini.bleacher.model.Photo
import com.rosahosseini.bleacher.remote.datasource.PhotoRemoteDataSource
import com.rosahosseini.bleacher.repository.SearchRepository
import com.rosahosseini.bleacher.repositoryimpl.extensions.isInDurationOf
import com.rosahosseini.bleacher.repositoryimpl.map.toPagePhotos
import com.rosahosseini.bleacher.repositoryimpl.map.toPhoto
import com.rosahosseini.bleacher.repositoryimpl.map.toSearchedPhotosEntities
import com.rosahosseini.bleacher.repositoryimpl.utils.RequestManager.Companion.Builder as RequestManagerBuilder
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchRepositoryImpl @Inject constructor(
    private val photosRemoteDataSource: PhotoRemoteDataSource,
    private val searchLocalDataSource: SearchPhotoLocalDataSource
) : SearchRepository {

    override suspend fun searchPhotos(
        query: String,
        pageNumber: Int,
        limit: Int
    ): Flow<Either<Page<Photo>>> = RequestManagerBuilder<Page<Photo>>()
        .netWorkCall { photosRemoteDataSource.search(query, limit, pageNumber).toPagePhotos() }
        .loadFromDb { searchLocalDataSource.search(query, pageNumber, limit).toPagePhotos(limit) }
        .saveCallResults { searchLocalDataSource.saveSearch(it.toSearchedPhotosEntities(query)) }
        .shouldFetch { it?.isDataOutDated() ?: true }
        .request()

    override suspend fun getRecentPhotos(
        pageNumber: Int,
        limit: Int
    ): Flow<Either<Page<Photo>>> = RequestManagerBuilder<Page<Photo>>()
        .netWorkCall { photosRemoteDataSource.getRecent(limit, pageNumber).toPagePhotos() }
        .loadFromDb {
            searchLocalDataSource.search(query = null, pageNumber, limit).toPagePhotos(limit)
        }
        .saveCallResults {
            searchLocalDataSource.saveSearch(it.toSearchedPhotosEntities(query = null))
        }
        .shouldFetch { it?.isDataOutDated() ?: true }
        .request()

    override fun searchLocalPhotos(query: String?): Flow<List<Photo>> {
        return searchLocalDataSource.searchFlow(query)
            .map { it.map { it.photoEntity.toPhoto() } }
    }

    private fun Page<Photo>.isDataOutDated(): Boolean {
        return timeStamp.isInDurationOf(DB_OUTDATED_MILLI_SECONDS).not()
    }

    companion object {
        private const val DB_OUTDATED_MILLI_SECONDS = 1000 * 60 * 30L
    }
}