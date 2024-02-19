package com.rosahosseini.findr.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "photo", indices = [Index(value = ["is_bookmarked"])])
data class PhotoEntity(
    @PrimaryKey
    val photoId: String,
    @ColumnInfo(name = "is_bookmarked")
    val isBookmarked: Boolean,
    val title: String? = null,
    val description: String? = null,
    val urlOriginal: String,
    val urlLarge: String? = null,
    val urlMedium800px: String? = null,
    val urlMedium640px: String? = null,
    val urlSmall320px: String? = null,
    val urlSmall240px: String? = null,
    val urlThumbnail150px: String? = null,
    val urlThumbnail100px: String? = null,
    val urlThumbnail75px: String? = null,
    val urlThumbnailSquare: String? = null,
    val timeStamp: Long
)
