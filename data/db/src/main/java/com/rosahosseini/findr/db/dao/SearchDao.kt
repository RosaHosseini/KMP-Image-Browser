package com.rosahosseini.findr.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rosahosseini.findr.db.entity.PhotoEntity
import com.rosahosseini.findr.db.entity.SearchEntity
import com.rosahosseini.findr.db.entity.SearchedPhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(searchedPhotos: List<SearchEntity>)

    @Query("SELECT * FROM photo WHERE photoId=:photoId LIMIT 1")
    suspend fun getPhoto(photoId: String): PhotoEntity?

    @Transaction
    @Query(
        "SELECT * FROM search_photo " +
            "WHERE queryText = :queryText " +
            "AND `offset` >= :from " +
            "AND `offset` < :to " +
            "ORDER BY `offset` ASC"
    )
    suspend fun search(queryText: String?, from: Int, to: Int): List<SearchedPhotoEntity>

    @Transaction
    @Query(
        "SELECT * FROM search_photo " +
            "WHERE queryText = :queryText " +
            "AND `offset` >= :from " +
            "AND `offset` < :to " +
            "ORDER BY `offset` ASC"
    )
    fun searchFlow(queryText: String?, from: Int, to: Int): Flow<List<SearchedPhotoEntity>>

    @Query("DELETE FROM search_photo WHERE timeStamp < :until")
    suspend fun clearExpiredSearch(until: Long)
}
