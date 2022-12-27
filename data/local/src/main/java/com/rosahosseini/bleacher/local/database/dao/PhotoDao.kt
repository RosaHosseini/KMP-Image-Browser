package com.rosahosseini.bleacher.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rosahosseini.bleacher.local.database.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(searchedPhotos: List<PhotoEntity>)

    @Query("SELECT * FROM photo WHERE photoId=:photoId LIMIT 1")
    suspend fun getPhoto(photoId: String): PhotoEntity?

    @Query("SELECT * FROM photo WHERE isBookmarked=1")
    fun getBookmarkedFlow(): Flow<List<PhotoEntity>>

    @Query("UPDATE photo set isBookmarked = :isBookmarked WHERE photoId=:photoId")
    suspend fun updateBookmark(photoId: String, isBookmarked: Boolean)

    @Query("DELETE from photo WHERE timeStamp < :until")
    suspend fun clearExpiredPhotos(until: Long)
}