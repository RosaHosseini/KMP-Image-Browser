package com.rosahosseini.findr.db.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.rosahosseini.findr.model.Page
import com.rosahosseini.findr.model.Photo

data class SearchedPhotoEntity(
    @Embedded
    val searchEntity: SearchEntity,
    @Relation(
        parentColumn = "photoId",
        entityColumn = "photoId"
    )
    val photoEntity: PhotoEntity
)

fun Page<Photo>.toSearchedPhotosEntities(query: String?): List<SearchedPhotoEntity> =
    items.mapIndexed { index, photo ->
        val hasMore = if (index + 1 == items.size) hasNext else true
        SearchedPhotoEntity(
            searchEntity = SearchEntity(
                queryText = query.orEmpty(),
                offset = pageNumber * pageSize + index,
                timeStamp = timeStamp,
                photoId = photo.id,
                hasMore = hasMore
            ),
            photoEntity = photo.toPhotoEntity(timeStamp)
        )
    }

fun List<SearchedPhotoEntity>.toPagePhotos(pageSize: Int): Page<Photo>? {
    if (isEmpty()) return null
    check(pageSize > 0) { "pageSize should be larger than zero" }
    return Page(
        items = map { it.photoEntity.toPhoto() },
        pageNumber = first().searchEntity.offset / pageSize,
        pageSize = pageSize,
        timeStamp = first().searchEntity.timeStamp,
        hasNext = last().searchEntity.hasMore
    )
}
