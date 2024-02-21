package com.rosahosseini.findr.domain.search

import com.rosahosseini.findr.model.Page
import com.rosahosseini.findr.model.Photo
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun searchPhotos(query: String, pageNumber: Int, pageSize: Int): Result<Page<Photo>>

    suspend fun getRecentPhotos(pageNumber: Int, pageSize: Int): Result<Page<Photo>>

    suspend fun clearExpiredData(expiredTimeMillis: Long)

    fun getSearchSuggestion(query: String, limit: Int): Flow<List<String>>

    suspend fun removeSearchQuery(query: String)

    suspend fun saveSearchQuery(query: String)
}
