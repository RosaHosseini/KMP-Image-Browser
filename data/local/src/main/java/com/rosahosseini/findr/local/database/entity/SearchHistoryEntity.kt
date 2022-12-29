package com.rosahosseini.findr.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey
    val queryText: String,
    val timeStamp: Long
)