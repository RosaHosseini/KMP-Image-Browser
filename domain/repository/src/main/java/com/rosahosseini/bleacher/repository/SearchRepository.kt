package com.rosahosseini.bleacher.repository

import com.rosahosseini.bleacher.model.Either
import com.rosahosseini.bleacher.model.Page
import com.rosahosseini.bleacher.model.Photo
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun searchPhotos(query: String, pageNumber: Int, limit: Int): Flow<Either<Page<Photo>>>

    suspend fun getRecentPhotos(pageNumber: Int, limit: Int): Flow<Either<Page<Photo>>>

    fun searchLocalPhotos(query: String?, fromPage: Int, toPage: Int, limit: Int): Flow<List<Photo>>

    suspend fun clearExpiredData(expiredTimeMillis: Long)

    fun getSearchSuggestion(query: String, limit: Int): Flow<List<String>>

    suspend fun removeSuggestion(query: String)

    suspend fun saveSearchQuery(query: String)
}