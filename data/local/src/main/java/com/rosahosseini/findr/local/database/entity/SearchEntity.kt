package com.rosahosseini.findr.local.database.entity

import androidx.room.Entity

@Entity(
    tableName = "search_photo",
    primaryKeys = ["queryText", "offset"]
)
data class SearchEntity(
    val queryText: String,
    val offset: Int,
    val photoId: String,
    val timeStamp: Long,
    val hasMore: Boolean = true
)
