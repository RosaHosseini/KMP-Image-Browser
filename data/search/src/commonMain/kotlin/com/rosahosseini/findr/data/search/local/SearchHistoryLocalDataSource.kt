package com.rosahosseini.findr.data.search.local

import com.rosahosseini.findr.db.dao.SearchHistoryDao
import com.rosahosseini.findr.db.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

internal class SearchHistoryLocalDataSource(
    private val searchHistoryDao: SearchHistoryDao
) {
    private fun getCurrentTimeMillis() = Clock.System.now().toEpochMilliseconds()

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
