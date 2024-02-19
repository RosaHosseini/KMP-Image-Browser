package com.rosahosseini.findr.repository.map

import com.rosahosseini.findr.core.extensions.getCurrentTimeMillis
import com.rosahosseini.findr.local.database.entity.SearchEntity
import com.rosahosseini.findr.local.database.entity.SearchedPhotoEntity
import com.rosahosseini.findr.model.Page
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.remote.model.response.SearchResponseDto

internal fun SearchResponseDto.toPagePhotos() = Page(
    items = data.photos?.map { it.toPhoto() }.orEmpty(),
    pageNumber = data.pageNumber - 1,
    pageSize = data.pageSize,
    hasNext = data.pageNumber < data.endPage,
    timeStamp = getCurrentTimeMillis()
)

internal fun Page<Photo>.toSearchedPhotosEntities(query: String?): List<SearchedPhotoEntity> =
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

internal fun List<SearchedPhotoEntity>.toPagePhotos(pageSize: Int): Page<Photo>? {
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
