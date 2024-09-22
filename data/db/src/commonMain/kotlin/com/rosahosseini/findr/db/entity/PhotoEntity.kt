package com.rosahosseini.findr.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rosahosseini.findr.domain.model.Photo
import kotlinx.datetime.Clock

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

fun Photo.toPhotoEntity(
    isBookmarked: Boolean,
    timeStamp: Long = Clock.System.now().toEpochMilliseconds()
) = PhotoEntity(
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
    title = title,
    description = description,
    url = url,
    thumbnailUrl = thumbnailUrl
)
