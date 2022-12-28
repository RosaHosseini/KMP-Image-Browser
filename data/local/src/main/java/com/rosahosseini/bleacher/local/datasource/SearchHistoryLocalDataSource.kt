package com.rosahosseini.bleacher.local.datasource

import com.rosahosseini.bleacher.core.extensions.getCurrentTimeMillis
import com.rosahosseini.bleacher.local.database.dao.SearchHistoryDao
import com.rosahosseini.bleacher.local.database.entity.SearchHistoryEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchHistoryLocalDataSource @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao,
) {
    suspend fun saveQuery(query: String) {
        searchHistoryDao.insertOrUpdate(SearchHistoryEntity(query, getCurrentTimeMillis()))
    }

    suspend fun clearExpiredQueries(expireTimeMillis: Long) {
        check(expireTimeMillis > 0) { "expire time should be positive" }
        val leastTimestamp = getCurrentTimeMillis() - expireTimeMillis
        searchHistoryDao.clearExpiredSearch(leastTimestamp)
    }

    fun getLatestQueries(query: String, limit: Int): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getLatestSearch(query, limit)
    }

    suspend fun removeQuery(query: String) {
        return searchHistoryDao.removeQuery(query)
    }
}