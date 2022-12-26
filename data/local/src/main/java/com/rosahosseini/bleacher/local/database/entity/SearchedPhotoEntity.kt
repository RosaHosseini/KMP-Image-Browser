package com.rosahosseini.bleacher.local.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SearchedPhotoEntity(
    @Embedded
    val searchEntity: SearchEntity,
    @Relation(
        parentColumn = "photoId",
        entityColumn = "photoId"
    )
    val photoEntity: PhotoEntity
)