package com.rosahosseini.findr.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class PhotoEntity(
    @PrimaryKey
    val photoId: String,
    val isBookmarked: Boolean,
    val title: String? = null,
    val description: String? = null,
    val urlOriginal: String? = null,
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