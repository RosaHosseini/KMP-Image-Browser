package com.rosahosseini.findr.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rosahosseini.findr.db.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo WHERE is_bookmarked=1 ORDER BY timeStamp DESC")
    fun getBookmarkedFlow(): Flow<List<PhotoEntity>>

    @Transaction
    suspend fun updateOrRemoveIfNotBookmark(photoEntity: PhotoEntity) {
        if (photoEntity.isBookmarked) {
            insertOrUpdate(photoEntity)
        } else {
            remove(photoEntity.photoId)
        }
    }

    @Query("SELECT * FROM photo WHERE photoId=:photoId LIMIT 1")
    suspend fun get(photoId: String): PhotoEntity?

    @Query("DELETE from photo WHERE photoId = :photoId")
    suspend fun remove(photoId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(photo: PhotoEntity)
}
