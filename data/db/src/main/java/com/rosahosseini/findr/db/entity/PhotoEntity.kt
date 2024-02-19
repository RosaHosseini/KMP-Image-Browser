package com.rosahosseini.findr.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rosahosseini.findr.model.Photo

@Entity(tableName = "photo", indices = [Index(value = ["is_bookmarked"])])
data class PhotoEntity(
    @PrimaryKey
    val photoId: String,
    @ColumnInfo(name = "is_bookmarked")
    val isBookmarked: Boolean,
    val title: String? = null,
    val description: String? = null,
    val url: String,
    val thumbnailUrl: String? = null,
    val timeStamp: Long
)

fun Photo.toPhotoEntity(timeStamp: Long) = PhotoEntity(
    photoId = id,
    isBookmarked = isBookmarked,
    title = title,
    description = description,
    url = url,
    thumbnailUrl = thumbnailUrl,
    timeStamp = timeStamp
)

fun PhotoEntity.toPhoto() = Photo(
    id = photoId,
    isBookmarked = isBookmarked,
    title = title,
    description = description,
    url = url,
    thumbnailUrl = thumbnailUrl
)
