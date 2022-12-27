package com.rosahosseini.bleacher.repositoryimpl.map

import com.rosahosseini.bleacher.core.extensions.getCurrentTimeMillis
import com.rosahosseini.bleacher.local.database.entity.SearchEntity
import com.rosahosseini.bleacher.local.database.entity.SearchedPhotoEntity
import com.rosahosseini.bleacher.model.Page
import com.rosahosseini.bleacher.model.Photo
import com.rosahosseini.bleacher.remote.model.response.SearchResponseDto

fun SearchResponseDto.toPagePhotos() = Page(
    items = data.photos?.map { it.toPhoto() }.orEmpty(),
    pageNumber = data.pageNumber - 1,
    pageSize = data.pageSize,
    hasNext = data.pageNumber < data.endPage,
    timeStamp = getCurrentTimeMillis()
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