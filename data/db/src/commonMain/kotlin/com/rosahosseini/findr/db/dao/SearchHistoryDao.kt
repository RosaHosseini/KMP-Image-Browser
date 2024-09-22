package com.rosahosseini.findr.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rosahosseini.findr.db.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(searchHistoryEntity: SearchHistoryEntity)

    // todo for some reason query does not work
    @Query(
        "SELECT * FROM search_history" +
            " WHERE queryText LIKE :query || '%' ESCAPE '\\'" +
            " OR queryText LIKE :query || '%'" +
            " ORDER BY timeStamp DESC " +
            " LIMIT :limit"
    )
    fun getLatestSearch(query: String, limit: Int): Flow<List<SearchHistoryEntity>>

    @Query("DELETE FROM search_history WHERE timeStamp < :until")
    suspend fun clearExpiredSearch(until: Long)

    @Query("DELETE FROM search_history WHERE queryText = :query")
    suspend fun removeQuery(query: String)
}
